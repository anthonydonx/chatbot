package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.entity.Answer;
import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.entity.Question;
import lk.esofttopup.chatbot.repository.CategoryRepository;
import lk.esofttopup.chatbot.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CategoryDataService {
    private final CategoryRepository categoryRepository;
    private final DoccatModel dataModel;







}
