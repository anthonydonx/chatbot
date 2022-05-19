package lk.esofttopup.chatbot.service;

import opennlp.tools.doccat.DoccatModel;

import java.io.IOException;
import java.util.List;

/**
 * NLP features implementation
 */
public interface NLPService {
    List<String> detectSentence(String inputText);

    String[] tokenize(String paragraph);

    void detectEntities() throws IOException;

    String[] posTag() throws IOException;

    //@PostConstruct
    String findCategory(String[] tokenize);

    String findCategory(String sentence);

    //DoccatModel trainCategoryDataset() throws IOException;
}
