package controller.rest.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Todo;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Helper methods for reading and writing JSON data.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class JsonHelper {
    public static final String CONTENT_TYPE = "application/json";
    public static final String ENCODING = "UTF-8";

    private static final Logger LOGGER = Logger.getLogger(JsonHelper.class.getName());

    /**
     * Reads JSON data
     *
     * @param requestBody the request body
     * @return a Map object
     */
    public static Map<String, ?> readJsonData(String requestBody) {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Read JSON data - - - - ");
        try {
            //noinspection unchecked
            return mapper.readValue(requestBody, Map.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Receives a list of todo items, formats the list to JSON format and returns a string.
     *
     * @param todoList a list of todo items
     * @return a JSON-formatted string with the todo list
     */
    public static String writeTodoJsonData(List<Todo> todoList) {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Write Todo JSON data - - - - ");
        try {
            ArrayNode node = mapper.createArrayNode();
            for (Todo todo : todoList) {
                ObjectNode objectNode = mapper.createObjectNode();
                addObjectNodes(todo, objectNode);
                node.add(objectNode);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Receives a list of category items, formats the list to JSON format and returns a string.
     *
     * @param categoryList a list of category items
     * @return a JSON-formatted string with the category list
     */
    public static String writeCategoryJsonData(List<String> categoryList) {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Write Category JSON data - - - - ");
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(categoryList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Receives a single todo item, formats it to JSON format and returns a string.
     *
     * @param todo a single todo item
     * @return a JSON-formatted string with the todo item
     */
    public static String writeTodoJsonData(Todo todo) {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.info(" - - - - Write Todo JSON data - - - - ");
        try {
            ObjectNode objectNode = mapper.createObjectNode();
            addObjectNodes(todo, objectNode);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addObjectNodes(Todo todo, ObjectNode objectNode) {
        objectNode.put("id", todo.getTodoID());
        objectNode.put("title", todo.getTitle());
        objectNode.put("category", todo.getCategory());
        String date = "";
        if (todo.getDueDate() != null) {
            date = todo.getDueDate().toString();
        }
        objectNode.put("dueDate", date);
        objectNode.put("important", todo.isImportant());
        objectNode.put("completed", todo.isCompleted());
    }
}
