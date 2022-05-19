package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.common.Category;
import lk.esofttopup.chatbot.common.ResponseType;
import lk.esofttopup.chatbot.dto.ChatBotAnswers;
import lk.esofttopup.chatbot.dto.ChatBotResponse;
import lk.esofttopup.chatbot.dto.ChatInput;
import lk.esofttopup.chatbot.dto.UserInputRequest;
import lk.esofttopup.chatbot.entity.Answer;
import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lk.esofttopup.chatbot.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ChatbotService {
    private final Map<String, String> informalDataMap;
    private final NLPService nlpService;
    private final CategoryRepository categoryRepository;

    public ChatBotResponse buildResponse(UserInputRequest userInputRequest) {
        List<ChatInput> chatInputList= userInputRequest.getChatInputs();
        List<ChatBotAnswers> chatBotAnswersList = new ArrayList<>();

        // get last input string
        String inputText = chatInputList.get(chatInputList.size()-1).getInputText();

        // Separate sentences
        List<String> sentenceList = nlpService.detectSentence(inputText);
        sentenceList.stream().forEach(sentence -> {
            // Find all words/tokens
            String[] tokenize = nlpService.tokenize(sentence);
            for (int i = 0; i < tokenize.length; i++) {
                if (informalDataMap.containsKey(tokenize[i])) { // informal word found,going to replace
                    tokenize[i] = informalDataMap.get(tokenize[i]).toLowerCase();
                }else{
                    tokenize[i]=tokenize[i].toLowerCase();
                }
            }
            String category = nlpService.findCategory(tokenize);
            if (category.equalsIgnoreCase(Category.greeting.name())) {
                Optional<CategoryData> allByCategoryName = categoryRepository.findAllByCategoryName(category);
                if (allByCategoryName.isPresent()) {
                    List<Answer> answers = allByCategoryName.get().getAnswers();
                    ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).text(answers.get(CommonUtil.getRandomNumber(answers.size())).getAnswer()).build();
                    chatBotAnswersList.add(chatBotAnswers);
                } else {
                    log.warn("Not define yet");
                }

            }
        });

        return ChatBotResponse.builder().answers(chatBotAnswersList).build();

    }
}
