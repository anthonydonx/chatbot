package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import org.springframework.transaction.annotation.Transactional;

//@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CategoryDataService {
    private final CategoryRepository categoryRepository;
    private final DoccatModel dataModel;







}
