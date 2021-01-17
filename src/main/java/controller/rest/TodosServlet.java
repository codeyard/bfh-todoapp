package controller.rest;

import model.Todo;
import model.User;
import model.UserException;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/todos")
public class TodosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String category = request.getParameter("category");
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        try {
            List<Todo> todos = new ArrayList<>();
            for (User user : userManager.getUsers()) {
                for (Todo todo : user.getTodos()) {
                    todos.add(todo);
                }
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
