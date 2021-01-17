package model.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Todo;

import java.util.List;
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

    public static String writeTodoJsonData(List<Todo> todoList){
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Write Todo JSON data - - - - ");
        try{
            ArrayNode node = mapper.createArrayNode();
            for(Todo todo : todoList){
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("id",todo.getTodoID());
                objectNode.put("title", todo.getTitle());
                objectNode.put("category", todo.getCategory());
                String date = "";
                if(todo.getDueDate() != null){
                    date = todo.getDueDate().toString();
                }
                objectNode.put("dueDate", date);
                objectNode.put("important", todo.isImportant());
                objectNode.put("completed", todo.isCompleted());
                node.add(objectNode);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
