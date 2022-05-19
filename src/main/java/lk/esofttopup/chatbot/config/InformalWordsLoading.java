package lk.esofttopup.chatbot.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
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
        Resource resource = new ClassPathResource("data/informal.json");
        List<HashMap> hashMaps = mapper.readValue(resource.getFile(), new TypeReference<List<HashMap>>() {
        });
        hashMaps.stream().forEach(hashMap -> {
            String key=hashMap.get(INFORMALWORD).toString().toLowerCase();
            String val=hashMap.get(FORMALWORD).toString().toLowerCase();
         informalMap.put(key,val);
        });
       return informalMap;
    }
}
