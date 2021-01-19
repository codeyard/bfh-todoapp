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
import java.util.Map;
import java.util.logging.Logger;

@WebServlet("/api/todos/*")
public class TodosRestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TodosRestServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String acceptType = request.getHeader("Accept");

        if (!acceptType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            LOGGER.warning(" - - - - Wrong content Type from Request: " + acceptType + " - - - - ");
        } else {
            String category = request.getParameter("category");
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                // todos/{id}
                try {
                    int todoID = Integer.parseInt(pathInfo.split("/")[1]);
                    User user = userManager.getUser((Integer) request.getAttribute("userID"));
                    Todo todo = user.getTodo(todoID);
                    if (todo != null) {
                        String json = JsonHelper.writeTodoJsonData(todo);
                        writeResponse(response, json, HttpServletResponse.SC_OK);
                        LOGGER.info(" - - - -  Response given - - - - ");
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        LOGGER.warning(" - - - - Resource not found: " + request.getPathInfo() + " - - - - ");
                    }
                } catch (Exception exception) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    LOGGER.warning(" - - - - Resource not found: " + request.getPathInfo() + " - - - - ");
                }
            } else {
                // todos without path parameter
                User user = userManager.getUser((Integer) request.getAttribute("userID"));
                String json = JsonHelper.writeTodoJsonData(user.getTodos(category));
                writeResponse(response, json, HttpServletResponse.SC_OK);
                LOGGER.info(" - - - -  Response given - - - - ");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        String acceptType = request.getHeader("Accept");

        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); // unsupported accept type
            LOGGER.warning(" - - - - Wrong content Type from Request: " + contentType + " - - - - ");
        } else if (!acceptType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            LOGGER.warning(" - - - - Wrong Accept Type from Request: " + acceptType + " - - - - ");
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                String body = request.getReader()
                        .lines()
                        .reduce("", (String::concat));
                Map<String, ?> map = JsonHelper.readJsonData(body);
                if (map != null && !map.isEmpty()) {
                    String title = (String) map.get("title");
                    if (title != null && !title.isEmpty()) {
                        String category = (map.get("category") != null) ? (String) map.get("category") : "";
                        String dueDate = (String) map.get("dueDate");
                        boolean isImportant = map.get("important") != null && (boolean) map.get("important");
                        boolean isCompleted = map.get("completed") != null && (boolean) map.get("completed");
                        LocalDate date = (dueDate != null && !dueDate.isEmpty()) ? LocalDate.parse(dueDate) : null;

                        User user = userManager.getUser((Integer) request.getAttribute("userID"));
                        Todo todo = new Todo(title, category, date, isImportant, isCompleted);
                        String todoId = todo.getTodoID().toString();
                        user.addTodo(todo);

                        XmlHelper.writeXmlData(userManager, servletContext);
                        writeResponse(response, todoId, HttpServletResponse.SC_CREATED);
                        LOGGER.warning(" - - - - Invalid Todo data  - - - - ");
                    } else {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        LOGGER.warning(" - - - - Bad request: " + request.getPathInfo() + " - - - - ");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    LOGGER.warning(" - - - - Bad request: " + request.getPathInfo() + " - - - - ");
                }
            } catch (Exception exception) {
                System.out.println("Exception: " + exception.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                LOGGER.warning(" - - - - Invalid Todo data  - - - - ");
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); // unsupported content type
            LOGGER.warning(" - - - - Wrong Content Type from Request: " + contentType + " - - - - ");
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                try {
                    int todoIDPath = Integer.parseInt(pathInfo.split("/")[1]);
                    Todo todo = null;
                    User user = userManager.getUser((Integer) request.getAttribute("userID"));
                    if (user.getTodo(todoIDPath) != null) {
                        todo = user.getTodo(todoIDPath);
                    }
                    if (todo != null) {
                        String body = request.getReader()
                                .lines()
                                .reduce("", (String::concat));
                        Map<String, ?> map = JsonHelper.readJsonData(body);
                        if (map != null && !map.isEmpty()) {
                            Integer todoIDBody = (Integer) map.get("id");
                            if (todoIDBody == null || todoIDPath == todoIDBody) {
                                if (map.get("title") != null && !((String) map.get("title")).isEmpty()) {
                                    String title = (String) map.get("title");
                                    String category = (String) map.get("category");
                                    String dueDate = (String) map.get("dueDate");

                                    LocalDate date = (dueDate != null && !dueDate.isEmpty()) ? LocalDate.parse(dueDate) : LocalDate.MIN;
                                    todo.setTitle(title);
                                    if (category != null && !category.isEmpty()) {
                                        todo.setCategory(category);
                                    }
                                    if (!date.isEqual(LocalDate.MIN)) {
                                        todo.setDueDate(date);
                                    }
                                    if (map.get("important") != null) {
                                        boolean isImportant = (boolean) map.get("important");
                                        todo.setImportant(isImportant);
                                    }
                                    if (map.get("completed") != null) {
                                        boolean isCompleted = (boolean) map.get("completed");
                                        todo.setCompleted(isCompleted);
                                    }
                                    user.updateTodo(todo);
                                    XmlHelper.writeXmlData(userManager, servletContext);
                                    writeResponse(response, "todoId", HttpServletResponse.SC_NO_CONTENT);
                                    LOGGER.info(" - - - - todo updated: " + todo.getTodoID() + " - - - - ");
                                } else {
                                    writeResponse(response, "", HttpServletResponse.SC_BAD_REQUEST);
                                    LOGGER.warning(" - - - - Invalid Todo data - - - - ");
                                }
                            } else {
                                writeResponse(response, "", HttpServletResponse.SC_BAD_REQUEST);
                                LOGGER.warning(" - - - - Invalid Todo data - - - - ");
                            }
                        } else {
                            writeResponse(response, "", HttpServletResponse.SC_BAD_REQUEST);
                            LOGGER.warning(" - - - - Invalid Todo data - - - - ");
                        }
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        LOGGER.warning(" - - - - Todo not found - - - - ");
                    }

                } catch (Exception exception) {
                    writeResponse(response, "", HttpServletResponse.SC_BAD_REQUEST);
                    LOGGER.warning(" - - - - Invalid Todo data - - - - ");
                }
            } else {
                writeResponse(response, "", HttpServletResponse.SC_BAD_REQUEST);
                LOGGER.warning(" - - - - Invalid Todo data - - - - ");
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            // todos/{id}
            try {
                Integer todoID = Integer.parseInt(pathInfo.split("/")[1]);
                User user = userManager.getUser((Integer) request.getAttribute("userID"));
                Todo todo = user.getTodo(todoID);
                if (todo != null) {
                    user.deleteTodo(todo);
                    XmlHelper.writeXmlData(userManager, servletContext);
                    writeResponse(response, "", HttpServletResponse.SC_NO_CONTENT);
                    LOGGER.info(" - - - - Todo removed id: " + todo.getTodoID() + "  - - - - ");
                } else {
                    writeResponse(response, "", HttpServletResponse.SC_NOT_FOUND);
                    LOGGER.warning(" - - - - Todo not found: " + pathInfo + "  - - - - ");
                }
            } catch (Exception e) {
                writeResponse(response, "", HttpServletResponse.SC_NOT_FOUND);
                LOGGER.warning(" - - - - Todo not found: " + pathInfo + "  - - - - ");
            }
        } else {
            writeResponse(response, "", HttpServletResponse.SC_NOT_FOUND);
            LOGGER.warning(" - - - - Todo not found: " + pathInfo + "  - - - - ");
        }
    }

    /**
     * Writes a json response
     *
     * @param response     - the servlets HttpServletResponse object
     * @param responseBody - the text to be written in the response (previoused parsed from jsom)
     * @param status       - Status code to be send
     * @throws IOException is thrown when the response couldn't be written
     */
    private void writeResponse(HttpServletResponse response, String responseBody, Integer status) throws IOException {
        response.setStatus(status);
        response.setContentType(JsonHelper.CONTENT_TYPE);
        response.setCharacterEncoding(JsonHelper.ENCODING);
        PrintWriter out = response.getWriter();
        out.print(responseBody);
        out.flush();
    }
}
