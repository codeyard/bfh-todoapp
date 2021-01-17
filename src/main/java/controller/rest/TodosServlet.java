package controller.rest;

import model.Todo;
import model.User;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/todos")
public class TodosServlet extends HttpServlet {
    private final static String CONTENT_TYPE = "application/json";
    private final static String ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(CONTENT_TYPE)) {
            response.setStatus(406); // unsupported accept type
            //TODO: 401 user not authorized to implement
        }else{
            String category = request.getParameter("category");
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                List<Todo> todos = new ArrayList<>();
                for (User user : userManager.getUsers()) {
                    for (Todo todo : user.getTodos(category)) {
                        todos.add(todo);
                    }
                }
                String json = JsonHelper.writeTodoJsonData(todos);
                response.setStatus(200);
                response.setContentType(CONTENT_TYPE);
                response.setCharacterEncoding(ENCODING);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
