package lk.esofttopup.chatbot.config;

import lk.esofttopup.chatbot.entity.Answer;
import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.entity.Question;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lk.esofttopup.chatbot.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.TrainingParameters;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
@Slf4j
public class DataModelLoading {
    private final CategoryRepository categoryRepository;

    /**
     * https://www.tabnine.com/code/java/methods/opennlp.tools.doccat.DocumentCategorizerME/%3Cinit%3E
     *
     * @return
     */
    @Bean(name = "DataModel")
    public DoccatModel trainCategoryDataset() throws IOException, InvalidFormatException {
        loadDataFromExcel();
        List<CategoryData> categoryDataList = categoryRepository.findAll();
        List<DocumentSample> documentSamples = new ArrayList<>();
        categoryDataList.stream().forEach(categoryData -> {
            List<String> dataListStr = new ArrayList<>();
            categoryData.getQuestions().forEach(question -> dataListStr.add(question.getQuestion()));

            //Create document per each sentence
            dataListStr.stream().forEach(s -> {
                documentSamples.add(new DocumentSample(categoryData.getCategoryName(),new String[]{s}));
            });

           // documentSamples.add(new DocumentSample(categoryData.getCategoryName(), dataListStr.stream().toArray(String[]::new)));

        });
        ObjectStream<DocumentSample> sampleObjectStream = ObjectStreamUtils.createObjectStream(documentSamples);
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 15);
        params.put(TrainingParameters.CUTOFF_PARAM, 1);

        TrainingParameters mlParams = new TrainingParameters();
        mlParams.put(TrainingParameters.ITERATIONS_PARAM, Integer.toString(150));
        mlParams.put(TrainingParameters.CUTOFF_PARAM, Integer.toString(0));

        DoccatModel model = DocumentCategorizerME.train("en", sampleObjectStream,
                mlParams, new DoccatFactory());
        return model;
    }

    public void loadDataFromExcel() throws IOException, InvalidFormatException {
        Workbook workbook = CommonUtil.loadExcelFile("/data/init_data.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, List<Question>> quListMap = new HashMap<>();
        Map<String, List<Answer>> ansListMap = new HashMap<>();
        int i = 1;
        String category = "";
        for (Row row : sheet) {
            log.info("Reading row number : {} ", i);

            String cat = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
            String question = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
            String answer = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";

            if (StringUtils.isNotEmpty(cat)) {
                category = cat;
            }

            log.info("Data going to be group category :{} question :{} answer :{}", category, question, answer);
            // Questions
            if (StringUtils.isNotEmpty(question)) {
                List<Question> orDefault = quListMap.getOrDefault(category, new ArrayList<>());
                orDefault.add(new Question(question));
                quListMap.put(category, orDefault);
            }
            // Answer
            if (StringUtils.isNotEmpty(answer)) {
                List<Answer> orDefault = ansListMap.getOrDefault(category, new ArrayList<>());
                orDefault.add(new Answer(answer));
                ansListMap.put(category, orDefault);
            }
            i++;

        }

        Map<String, CategoryData> categoryDataMap = new HashMap<>();
        quListMap.keySet().forEach(key -> {
            CategoryData categoryData = new CategoryData();
            categoryData.setCategoryName(key);
            categoryData.setAnswers(ansListMap.getOrDefault(key, new ArrayList<>()));
            categoryData.setQuestions(quListMap.getOrDefault(key, new ArrayList<>()));
            categoryDataMap.put(key, categoryData);
        });
        categoryDataMap.keySet().forEach(key -> log.info("Category :{} values :{}", key, categoryDataMap.get(key)));
        saveAllCategories(categoryDataMap.values().stream().collect(Collectors.toList()));
      /*  categoryDataMap.values().forEach(categoryData -> {
            categoryRepository.save(new CategoryData())
        });*/
    }

    List<CategoryData> saveAllCategories(List<CategoryData> categoryDataList) {
        List<CategoryData> categoryData = categoryRepository.saveAll(categoryDataList);
        log.info("{} Records update on category table. ", categoryData.size());
        return categoryData;
    }

}
