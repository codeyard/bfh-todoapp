package model.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.logging.Logger;

public class JsonHelper {

    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getName());

    public static Map<String, String> readJsonData(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> map = mapper.readValue(requestBody, Map.class);
            return map;
        } catch (JsonProcessingException e) {
            return null;
        }



    }


}
