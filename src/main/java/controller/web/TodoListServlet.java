package controller.web;

import model.Todo;
import model.User;
import model.UserManager;
import model.helper.XmlHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * A servlet for reading, filtering and deleting multiple todo items.
 * Listens to "/todos" path.
 * Redirects to the login page if no user is present/authorized.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet("/todos")
public class TodoListServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TodoListServlet.class.getName());

    /**
     * Displays multiple todo items.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException      if unable to forward to view
     * @throws ServletException if unable to forward to view
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher view;
        if (user == null) {
            response.reset();
            response.sendRedirect("login");
            LOGGER.info(" - - - - User not logged in  - - - - ");
        } else {
            try {
                request.setAttribute("todos", user.getTodos());
                view = request.getRequestDispatcher("todos.jsp");
                view.forward(request, response);
                LOGGER.info(" - - - - Getting users todo list  - - - - ");
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
            }
        }
    }

    /**
     * Filters multiple todos items when category or/and status parameter is present.
     * Deletes all completed todo items when the corresponding button is pressed.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException if unable to forward to view
     * @throws IOException      if unable to forward to view
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String deleteTodos = request.getParameter("deleteCompletedTodos");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher view;
        if (user == null) {
            response.reset();
            response.sendRedirect("login");
            LOGGER.info(" - - - - User not logged in  - - - - ");
        } else {
            if ("Delete completed Todos".equals(deleteTodos)) {
                deleteCompletedTodos(user);
                ServletContext servletContext = getServletContext();
                UserManager userManager = UserManager.getInstance(servletContext);
                XmlHelper.writeXmlData(userManager, servletContext);
            }

            request.setAttribute("todos", user.getTodos(category, status));
            request.setAttribute("categoryFilter", category);
            request.setAttribute("statusFilter", status);
            boolean listIsFiltered = ((category != null && !category.isEmpty()) || (status != null && !status.isEmpty())) ? true : false;
            request.setAttribute("listIsFiltered", listIsFiltered);

            try {
                view = request.getRequestDispatcher("todos.jsp");
                view.forward(request, response);
                LOGGER.info(" - - - - Filtering user list  - - - - ");
            } catch (ServletException | IOException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
            }
        }
    }

    private void deleteCompletedTodos(User user) {
        List<Todo> todos = user.getTodos().stream().filter(Todo::isCompleted).collect(Collectors.toList());
        if (todos.size() > 0) {
            for (Todo todo : todos) {
                user.deleteTodo(todo);
            }
        }

    }
}
