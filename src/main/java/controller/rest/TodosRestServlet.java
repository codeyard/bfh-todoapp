package controller.rest;

import model.Todo;
import model.User;
import model.UserManager;
import model.helper.JsonHelper;
import model.helper.XmlHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/todos/*")
public class TodosRestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(JsonHelper.STATUS_406); // unsupported accept type
            //TODO: 401 user not authorized to implement
        } else {
            String category = request.getParameter("category");
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            String pathInfo = request.getPathInfo();
            Integer todoID;
            if (pathInfo != null && !pathInfo.isEmpty()) {
                // todos/{id}
                try {
                    todoID = Integer.parseInt(pathInfo.split("/")[1]);
                    Todo todo = null;
                    for (User user : userManager.getUsers()) {
                        // TODO: check if user is authorized and only return his todo
                        if (user.getTodo(todoID) != null) {
                            todo = user.getTodo(todoID);
                            break;
                        }
                    }
                    if (todo != null) {
                        String json = JsonHelper.writeTodoJsonData(todo);
                        writeResponse(response, json, JsonHelper.STATUS_200);
                    } else {
                        response.setStatus(JsonHelper.STATUS_404); // todo not found
                    }
                } catch (Exception exception) {
                    response.setStatus(JsonHelper.STATUS_404); // todo not found
                }
            } else {
                // todos without path parameter
                try {
                    List<Todo> todos = new ArrayList<>();
                    for (User user : userManager.getUsers()) {
                        todos.addAll(user.getTodos(category));
                    }
                    String json = JsonHelper.writeTodoJsonData(todos);
                    writeResponse(response, json, JsonHelper.STATUS_200);
                } catch (Exception e) {
                    //TODO: what Exception are we catching here -> what should the response be?400?
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(JsonHelper.STATUS_406); // unsupported accept type
            //TODO: 401 user not authorized to implement
            //TODO: 415 what's the difference between 406 and 415?
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Map<String, ?> map = JsonHelper.readJsonData(body);
                if (map != null && !map.isEmpty()){
                    String title = (String) map.get("title");
                    if (title != null && !title.isEmpty()) {
                        String category = (map.get("category") != null) ? (String) map.get("category") : "";
                        String dueDate = (String) map.get("dueDate");
                        boolean isImportant = map.get("important") != null && (boolean) map.get("important");
                        boolean isCompleted = map.get("completed") != null && (boolean) map.get("completed");
                        LocalDate date = (dueDate != null && !dueDate.isEmpty()) ? LocalDate.parse(dueDate) : null;

                        //TODO: replace this, as we don't have the right user yet
                        //TODO: 401
                        User user = userManager.getUsers().stream()
                                .findFirst()
                                .get();
                        Todo todo = new Todo(title, category, date, isImportant, isCompleted);
                        String todoId = todo.getTodoID().toString();
                        user.addTodo(todo);
                        //TODO: until here

                        XmlHelper.writeXmlData(userManager, servletContext);
                        writeResponse(response, todoId, JsonHelper.STATUS_201);
                    } else {
                        response.setStatus(JsonHelper.STATUS_400); // invalid todo data
                    }
                } else {
                    //TODO: check if this is correct? 415 when map is empty?
                    response.setStatus(JsonHelper.STATUS_415); // invalid todo data
                }
            } catch (Exception exception) {
                System.out.println("Exception: " + exception.getMessage());
                response.setStatus(JsonHelper.STATUS_400); // invalid todo data
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(JsonHelper.STATUS_415); // unsupported content type
            //TODO: 401 user not authorized to implement
            //TODO: 415 what's the difference between 406 and 415?
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            String pathInfo = request.getPathInfo();
            Integer todoIDPath;
            if (pathInfo != null && !pathInfo.isEmpty()) {
                try{
                    todoIDPath = Integer.parseInt(pathInfo.split("/")[1]);
                    Todo todo = null;
                    Integer userID = null;
                    for (User user : userManager.getUsers()) {
                        // TODO: check if user is authorized and only return his todo
                        if (user.getTodo(todoIDPath) != null) {
                            todo = user.getTodo(todoIDPath);
                            userID = user.getUserID();
                            break;
                        }
                    }
                    if (todo != null) {
                        String body = request.getReader()
                                .lines()
                                .reduce("", (String::concat));
                        Map<String, ?> map = JsonHelper.readJsonData(body);
                        if (map != null && !map.isEmpty()) {
                            Integer todoIDBody = (Integer) map.get("id");
                            if(todoIDPath.compareTo(todoIDBody) == 0){
                                if(map.get("title") != null && !((String)map.get("title")).isEmpty()){
                                    String title = (String) map.get("title");
                                    String category = (String) map.get("category");
                                    String dueDate = (String) map.get("dueDate");

                                    LocalDate date = (dueDate != null && !dueDate.isEmpty()) ?LocalDate.parse(dueDate) :LocalDate.MIN;
                                    // TODO: check if this has still to be done like this after authentication of user
                                    for (User user : userManager.getUsers()) {
                                        if(userID.compareTo(user.getUserID()) == 0){
                                            Todo tempTodo = user.getTodo(todoIDPath);
                                            tempTodo.setTitle(title);
                                            if(category != null && !category.isEmpty()){
                                                tempTodo.setCategory(category);
                                            }
                                            if(!date.isEqual(LocalDate.MIN)){
                                                tempTodo.setDueDate(date);
                                            }
                                            if(map.get("important") != null){
                                                boolean isImportant = map.get("important") != null && (boolean) map.get("important");
                                                tempTodo.setImportant(isImportant);
                                            }
                                            if(map.get("completed") != null){
                                                boolean isCompleted = map.get("completed") != null && (boolean) map.get("completed");
                                                tempTodo.setImportant(isCompleted);
                                            }
                                            user.updateTodo(tempTodo);
                                            XmlHelper.writeXmlData(userManager, servletContext);
                                            writeResponse(response, "todoId", JsonHelper.STATUS_204);
                                            break;
                                        }
                                    }
                                } else {
                                    writeResponse(response, "", JsonHelper.STATUS_400); // invalid todo data
                                }
                            } else {
                                writeResponse(response, "", JsonHelper.STATUS_400); // invalid todo data
                            }
                        } else {
                            writeResponse(response, "", JsonHelper.STATUS_400); // invalid todo data
                        }
                    } else {
                        response.setStatus(JsonHelper.STATUS_404); // todo not found
                    }
                } catch (Exception exception){
                    writeResponse(response, "", JsonHelper.STATUS_400); // invalid todo data
                }
            } else {
                writeResponse(response, "", JsonHelper.STATUS_400); // invalid todo data
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            // todos/{id}
            try {
                Integer todoID = Integer.parseInt(pathInfo.split("/")[1]);
                System.out.println("Path: " + todoID);
                for (User user : userManager.getUsers()) {
                    // TODO: check if user is authorized and only return his todo
                    if (user.getTodo(todoID) != null) {
                        Todo todo = user.getTodo(todoID);
                        user.deleteTodo(todo);
                        XmlHelper.writeXmlData(userManager, servletContext);
                        writeResponse(resp, "", JsonHelper.STATUS_204);
                        return;
                    }
                }
                writeResponse(resp, "", JsonHelper.STATUS_404); // todo not found
            } catch (Exception e) {
                writeResponse(resp, "", JsonHelper.STATUS_404); // todo not found
            }
        } else{
            writeResponse(resp, "", JsonHelper.STATUS_404); // todo not found
        }
    }

    private void writeResponse(HttpServletResponse response, String responseBody, Integer status) throws IOException {
        response.setStatus(status);
        response.setContentType(JsonHelper.CONTENT_TYPE);
        response.setCharacterEncoding(JsonHelper.ENCODING);
        PrintWriter out = response.getWriter();
        out.print(responseBody);
        out.flush();
    }
}
