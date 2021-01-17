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
    private final static String CONTENT_TYPE = "application/json";
    private final static String ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(CONTENT_TYPE)) {
            response.setStatus(406); // unsupported accept type
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
                    System.out.println("Path: " + todoID);
                    for (User user : userManager.getUsers()) {
                        // TODO: check if user is authorized and only return his todo
                        if (user.getTodo(todoID) != null) {
                            todo = user.getTodo(todoID);
                            System.out.println(todo.getTodoID());
                            break;
                        }
                    }
                    if (todo == null) {
                        System.out.println("Todo null");
                        response.setStatus(404); // todo not found
                        return;
                    }
                    String json = JsonHelper.writeTodoJsonData(todo);
                    writeResponse(response, json, 200);
                    return;
                } catch (Exception exception) {
                    System.out.println("exception");
                    response.setStatus(404); // todo not found
                    return;
                }
            } else {
                // todos without path parameter
                try {
                    List<Todo> todos = new ArrayList<>();
                    for (User user : userManager.getUsers()) {
                        for (Todo todo : user.getTodos(category)) {
                            todos.add(todo);
                        }
                    }
                    String json = JsonHelper.writeTodoJsonData(todos);
                    writeResponse(response, json, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void writeResponse(HttpServletResponse response, String responseBody, Integer status) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(ENCODING);
        PrintWriter out = response.getWriter();
        out.print(responseBody);
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(CONTENT_TYPE)) {
            response.setStatus(406); // unsupported accept type
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
                String title = (String) map.get("title");
                if (title != null && !title.isEmpty()) {
                    String category = (String) map.get("category");
                    String dueDate = (String) map.get("dueDate");
                    boolean isImportant = (boolean) map.get("important");
                    boolean isCompleted = (boolean) map.get("completed");

                    LocalDate date = LocalDate.parse(dueDate);


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
                    writeResponse(response, todoId, 201);
                    return;
                } else {
                    response.setStatus(400); // invalid todo data
                    return;
                }
            } catch (Exception exception) {
                response.setStatus(400); // invalid todo data
                return;
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        String pathInfo = req.getPathInfo();
        Integer todoID;
        if (pathInfo != null && !pathInfo.isEmpty()) {
            // todos/{id}
            try {
                todoID = Integer.parseInt(pathInfo.split("/")[1]);
                Todo todo = null;
                System.out.println("Path: " + todoID);
                for (User user : userManager.getUsers()) {
                    // TODO: check if user is authorized and only return his todo
                    if (user.getTodo(todoID) != null) {
                        todo = user.getTodo(todoID);
                        user.deleteTodo(todo);
                        XmlHelper.writeXmlData(userManager, servletContext);
                        writeResponse(resp, "", 204);
                        return;
                    }
                }
                writeResponse(resp, "", 404); // todo not found
            } catch (Exception e) {
                writeResponse(resp, "", 404); // todo not found
            }

        }
    }
}
