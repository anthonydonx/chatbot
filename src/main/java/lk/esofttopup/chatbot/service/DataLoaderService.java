package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.dataset.QuestionsDataSet;

import java.util.List;

public interface DataLoaderService<T> {
    List<T> loadFromResource();
}
