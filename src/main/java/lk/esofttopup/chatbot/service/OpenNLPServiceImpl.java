package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class OpenNLPServiceImpl implements NLPService {
    private final CategoryRepository categoryRepository;

    private final DoccatModel dataModel;

    @Override
    public List<String> detectSentence() throws IOException {
        return null;
    }

    @Override
    public String[] tokenize(String input) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizerME = new TokenizerME(model);
        String[] tokenize = tokenizerME.tokenize(input);
        Arrays.stream(tokenize).forEach(token -> log.info("Token found :{}", token));
        return tokenize;
    }

    @Override
    public void detectEntities() throws IOException {

    }

    @Override
    public String[] posTag() throws IOException {
        return new String[0];
    }

    //@PostConstruct
    String findCategory() throws IOException {


        //String paragraph = "Can I get more details about the course";
        //String paragraph = "Please send me details about this course";
        //String paragraph = "Please tell me about course information";
        //String paragraph = "Masters?";
        //String paragraph = "What are the entry requirement?";
        //String paragraph = "Let me know entry requirement?";
        //String paragraph = "Is it possible";
        String paragraph = "Is it Masters?";
        //String paragraph = "Hi";
        DocumentCategorizerME categorizerME = new DocumentCategorizerME(dataModel);
        for (int i = 0; i < categorizerME.getNumberOfCategories(); i++) {
            log.info("Trained Categories : {}", categorizerME.getCategory(i));
        }
        double[] categorize = categorizerME.categorize(tokenize(paragraph));
        //double[] categorize = categorizerME.categorize(new String[]{paragraph});
        String bestCategory = categorizerME.getBestCategory(categorize);
        //Arrays.sort(categorize);

        log.info("All categories , {} ", categorizerME.getAllResults(categorize));

        //SortedMap<Double, Set<String>> doubleSetSortedMap = categorizerME.sortedScoreMap(tokenize(paragraph));

        //doubleSetSortedMap.forEach((aDouble, strings) -> log.info("Value is : {} & Categories : {}", aDouble, strings));

        // log.info("All categories : {}",categorize);
        log.info("Best Category : {}", bestCategory);
        return bestCategory;


    }
}
