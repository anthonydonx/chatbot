package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OpenNLPServiceImpl implements NLPService {
    private final CategoryRepository categoryRepository;

    private final DoccatModel dataModel;

    @Override
    public List<String> detectSentence(String inputText) {
        try {
            InputStream is = getClass().getResourceAsStream("/en-sent.bin");
            SentenceModel model = new SentenceModel(is);
            SentenceDetectorME detectorME = new SentenceDetectorME(model);
            String[] strings = detectorME.sentDetect(inputText);
            log.info("sentence detect {}",strings);
            return Arrays.stream(strings).collect(Collectors.toList());
        } catch (IOException ex) {
            log.error("sentence detection error", ex);
            return new ArrayList<>(Arrays.asList(inputText));
        }
    }

    @Override
    public String[] tokenize(String input) {
        try{
        InputStream inputStream = getClass().getResourceAsStream("/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizerME = new TokenizerME(model);
        String[] tokenize = tokenizerME.tokenize(input);
        Arrays.stream(tokenize).forEach(token -> log.info("Token found :{}", token));
        return tokenize;
        } catch (IOException ex) {
            log.error("Tokenize detection error", ex);
            return new String[]{input};
        }
    }

    @Override
    public void detectEntities() throws IOException {

    }

    @Override
    public String[] posTag() throws IOException {
        return new String[0];
    }

    //@PostConstruct
    @Override
    public String findCategory(String[] tokenize){


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
        double[] categorize = categorizerME.categorize(tokenize);
        String bestCategory = categorizerME.getBestCategory(categorize);
        log.info("All categories , {} ", categorizerME.getAllResults(categorize));
        log.info("Best Category : {}", bestCategory);
        return bestCategory;


    }
    @Override
    public String findCategory(String sentence){

        DocumentCategorizerME categorizerME = new DocumentCategorizerME(dataModel);
        for (int i = 0; i < categorizerME.getNumberOfCategories(); i++) {
            log.info("Trained Categories : {}", categorizerME.getCategory(i));
        }
        double[] categorize = categorizerME.categorize(new String[]{sentence});
        String bestCategory = categorizerME.getBestCategory(categorize);
        log.info("All categories , {} ", categorizerME.getAllResults(categorize));
        log.info("Best Category : {}", bestCategory);
        return bestCategory;


    }
}
