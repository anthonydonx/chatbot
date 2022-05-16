package lk.esofttopup.chatbot.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.esofttopup.chatbot.dataset.QuestionsDataSet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class CommonUtil {
    //@PostConstruct
    public void readFileAsString() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        Resource resource=new ClassPathResource("data/questions.json");
        List<QuestionsDataSet> questionsDataSets = mapper.readValue(resource.getFile(), new TypeReference<List<QuestionsDataSet>>() {});

    }
    public static Workbook loadExcelFile(String fileLocation) throws IOException, InvalidFormatException {
        Resource resource=new ClassPathResource(fileLocation);
        Workbook workbook = new XSSFWorkbook(resource.getFile());
        return workbook;
    }
}
