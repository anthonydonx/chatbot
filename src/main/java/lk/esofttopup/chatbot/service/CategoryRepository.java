package lk.esofttopup.chatbot.service;

import lk.esofttopup.chatbot.entity.CategoryData;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public interface CategoryRepository {
    List<CategoryData> loadDataFromExcel() throws IOException, InvalidFormatException;
}
