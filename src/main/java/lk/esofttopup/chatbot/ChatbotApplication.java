package lk.esofttopup.chatbot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.esofttopup.chatbot.common.Category;
import lk.esofttopup.chatbot.dataset.QuestionsDataSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatbotApplication.class, args);
	}
//@PostConstruct
	public void generateJSON() throws JsonProcessingException {
	List<QuestionsDataSet> dataSets=new ArrayList<>();
	for (int i = 0; i < 3; i++) {
		QuestionsDataSet trainData=new QuestionsDataSet();
		trainData.setCategory(Category.GREETING.name());
		trainData.setQuestions(Arrays.asList("Hi","Hello","Are you there?"));
		trainData.setAnswers(Arrays.asList("How can i help you","Need any help?"));
		dataSets.add(trainData);
	}

		System.out.println(new ObjectMapper().writeValueAsString(dataSets));

	}
}
