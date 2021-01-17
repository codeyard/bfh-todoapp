package model.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.logging.Logger;

public class JsonHelper {

    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getName());

    public static Map<String, String> readJsonData(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Read JSON data - - - - ");
        try {
            return mapper.readValue(requestBody, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static String writeJsonData()
}
