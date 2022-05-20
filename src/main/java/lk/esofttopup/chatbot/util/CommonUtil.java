package lk.esofttopup.chatbot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Component
public class CommonUtil {
    //@PostConstruct
    public void readFileAsString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = new ClassPathResource("data/questions.json");
        //List<QuestionsDataSet> questionsDataSets = mapper.readValue(resource.getFile(), new TypeReference<List<QuestionsDataSet>>() {});

    }

    public static Workbook loadExcelFile(InputStream fileLocation) throws IOException, InvalidFormatException {
        //Resource resource = new ClassPathResource(fileLocation);
        Workbook workbook = new XSSFWorkbook(fileLocation);
        return workbook;
    }

    /**
     * get random number between 0 to max
     * @param maxValue
     * @return
     */
    public static int getRandomNumber(int maxValue) {
        Random random = new Random();
        return random.nextInt(maxValue);
    }
}
