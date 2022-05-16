package lk.esofttopup.chatbot.service;

import opennlp.tools.doccat.DoccatModel;

import java.io.IOException;
import java.util.List;

/**
 * NLP features implementation
 */
public interface NLPService {
    List<String> detectSentence() throws IOException;

    String[] tokenize(String paragraph) throws IOException;

    void detectEntities() throws IOException;

    String[] posTag() throws IOException;

    //DoccatModel trainCategoryDataset() throws IOException;
}
