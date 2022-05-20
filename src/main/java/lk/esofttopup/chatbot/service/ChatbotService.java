package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.common.Category;
import lk.esofttopup.chatbot.common.ResponseType;
import lk.esofttopup.chatbot.dto.ChatBotAnswers;
import lk.esofttopup.chatbot.dto.ChatBotResponse;
import lk.esofttopup.chatbot.dto.ChatInput;
import lk.esofttopup.chatbot.dto.UserInputRequest;
import lk.esofttopup.chatbot.entity.Answer;
import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.entity.course.Course;
import lk.esofttopup.chatbot.entity.course.Faculty;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lk.esofttopup.chatbot.repository.FacultyRepository;
import lk.esofttopup.chatbot.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChatbotService {
    public static final String NOT_FOUND_ANY_CATEGORY = "Sorry, I didn't understand your question. Can you type again or rephrase it please?";
    private final Map<String, String> informalDataMap;
    private final NLPService nlpService;
    private final CategoryRepository categoryRepository;
    private final FacultyRepository facultyRepository;

    public ChatBotResponse buildResponse(UserInputRequest userInputRequest) {
        List<ChatInput> chatInputList = userInputRequest.getChatInputs();
        List<ChatBotAnswers> chatBotAnswersList = new ArrayList<>();

        // get last input string
        ChatInput chatInput = chatInputList.get(chatInputList.size() - 1);
        String inputText = chatInput.getInputText();

        // Separate sentences
        List<String> sentenceList = nlpService.detectSentence(inputText);
        if (sentenceList.size() == 0 && StringUtils.isNotEmpty((chatInput.getFacultyId()))) {
            // no sentence and faculty selected means looking for course
            List<ChatBotAnswers> responseList = askingCourseDetails(chatInput, Category.course_details.name());
            chatBotAnswersList.addAll(responseList);
            return ChatBotResponse.builder().answers(chatBotAnswersList).build();

        }
        sentenceList.stream().forEach(sentence -> {
            // Find all words/tokens
            String[] tokenize = nlpService.tokenize(sentence);
            for (int i = 0; i < tokenize.length; i++) {
                if (informalDataMap.containsKey(tokenize[i])) { // informal word found,going to replace
                    tokenize[i] = informalDataMap.get(tokenize[i]).toLowerCase();
                } else {
                    tokenize[i] = tokenize[i].toLowerCase();
                }
            }
            String category = nlpService.findCategory(tokenize);
            //String category = nlpService.findCategory(sentence);

            // @Todo better to go with switch case
            if (category.equalsIgnoreCase(Category.greeting.name())) {
                // Get all relevant answers from database by identified category
                List<Answer> answers = getFromDatasource(category);

                // Build response with random answer from list
                ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).
                        text(answers.get(CommonUtil.getRandomNumber(answers.size())).getAnswer()).build();
                // add to the response list
                chatBotAnswersList.add(chatBotAnswers);

                //Online
            } else if (category.equalsIgnoreCase(Category.online.name())) {
                List<Answer> answers = getFromDatasource(Category.online.name());
                ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).
                        text(answers.get(CommonUtil.getRandomNumber(answers.size())).getAnswer()).build();
                // add to the response list
                chatBotAnswersList.add(chatBotAnswers);
            } else if (category.equalsIgnoreCase(Category.apply_for_student_loan.name())) {
                List<Answer> answers = getFromDatasource(Category.apply_for_student_loan.name());
                ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).
                        text(answers.get(CommonUtil.getRandomNumber(answers.size())).getAnswer()).build();
                // add to the response list
                chatBotAnswersList.add(chatBotAnswers);
            } else if (category.equalsIgnoreCase(Category.thanks_reply.name())) {
                List<Answer> answers = getFromDatasource(Category.thanks_reply.name());
                ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).
                        text(answers.get(CommonUtil.getRandomNumber(answers.size())).getAnswer()).build();
                // add to the response list
                chatBotAnswersList.add(chatBotAnswers);

            } else if (category.equalsIgnoreCase(Category.course_details.name())) {
                List<ChatBotAnswers> responseList = askingCourseDetails(chatInput, category);
                chatBotAnswersList.addAll(responseList);
            } else if (category.equalsIgnoreCase(Category.course_fee.name())) {
                Faculty faculty = facultyRepository.getById(Long.valueOf(chatInput.getFacultyId()));
                List<Course> list = faculty.getCourseList().stream().filter(course -> String.valueOf(course.getId()).equalsIgnoreCase( chatInput.getCourseId())).collect(Collectors.toList());

                /*Course courseSel = null;
                for (Course course:faculty.getCourseList()) {
                    if(String.valueOf(course.getId())==chatInput.getCourseId()){
                        courseSel=course;
                        break;
                    }
                }*/
                ChatBotAnswers build = ChatBotAnswers.builder().type(ResponseType.TEXT).text(list.get(0).getCourseFee()).build();
                chatBotAnswersList.add(build);
            } else {
                ChatBotAnswers chatBotAnswers = ChatBotAnswers.builder().type(ResponseType.TEXT).
                        text(NOT_FOUND_ANY_CATEGORY).build();
                // add to the response list
                chatBotAnswersList.add(chatBotAnswers);
            }
        });

        return ChatBotResponse.builder().answers(chatBotAnswersList).build();

    }

    private List<ChatBotAnswers> askingCourseDetails(ChatInput chatInput, String category) {
        List<ChatBotAnswers> chatBotAnswersList = new ArrayList<>();
        // check course already selected
        String courseId = chatInput.getCourseId();
        String facultyId = chatInput.getFacultyId();
        if (StringUtils.isEmpty(facultyId)) {
            // faculty not selected
            List<Faculty> all = facultyRepository.findAll();
            for (Faculty faculty : all) {
                ChatBotAnswers chatBotAnswer = ChatBotAnswers.builder().type(ResponseType.BUTTON).inputId(String.valueOf(faculty.getId())).
                        text(faculty.getName()).listType("faculty_details").commonText("Please select faculty from list above").build();
                chatBotAnswersList.add(chatBotAnswer);
            }
            return chatBotAnswersList;

        } else if (StringUtils.isEmpty(courseId)) {
            // course not selected
            Faculty faculty = facultyRepository.getById(Long.valueOf(facultyId));
            List<Course> courseList = faculty.getCourseList();
            for (Course course : courseList) {
                ChatBotAnswers chatBotAnswer = ChatBotAnswers.builder().text(course.getName()).
                        type(ResponseType.BUTTON).listType(category).inputId(String.valueOf(course.getId()))
                        .commonText(("Please elect course from list above")).build();
                chatBotAnswersList.add(chatBotAnswer);
            }
        } else if (StringUtils.isNotEmpty(courseId) && StringUtils.isNotEmpty(facultyId)) {
            ChatBotAnswers build = ChatBotAnswers.builder().type(ResponseType.TEXT).text("Thank you. Let me know what you want to know about course?").build();
            chatBotAnswersList.add(build);
        } else {
            ChatBotAnswers build = ChatBotAnswers.builder().type(ResponseType.TEXT).text("Something going wrong please reload the page").build();
            chatBotAnswersList.add(build);
        }

        return chatBotAnswersList;
    }

    private List<Answer> getFromDatasource(String category) {
        Optional<CategoryData> allByCategoryName = categoryRepository.findAllByCategoryName(category);
        if (allByCategoryName.isPresent()) {
            // get all list of answers
            return allByCategoryName.get().getAnswers();

        }
        return new ArrayList<>();
    }
}
