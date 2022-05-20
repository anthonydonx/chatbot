package lk.esofttopup.chatbot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
@Slf4j
public class InformalWordsLoading {

    public static final String INFORMALWORD = "informalword";
    public static final String FORMALWORD = "formalword";

    @Bean(name = "informalDataMap")
    public Map<String,String> informalDataMap() throws IOException {
        Map<String, String> informalMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        InputStream resource = getClass().getResourceAsStream(File.separator + "data" + File.separator + "informal.json");
        log.info("Informal resource json file path :{} ",resource);
        //String file = resource.getFile();
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        List<HashMap> hashMaps = mapper.readValue(resource, new TypeReference<List<HashMap>>() {
        });
        hashMaps.stream().forEach(hashMap -> {
            String key=hashMap.get(INFORMALWORD).toString().toLowerCase();
            String val=hashMap.get(FORMALWORD).toString().toLowerCase();
         informalMap.put(key,val);
        });
       return informalMap;
    }
}
