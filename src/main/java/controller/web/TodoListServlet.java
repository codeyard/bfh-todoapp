package controller.web;

import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/todos")
public class TodoListServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.reset();
            response.sendRedirect("login");
        } else {
            try {
                request.setAttribute("todos", user.getTodos());
                RequestDispatcher view = request.getRequestDispatcher("todos.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String category = request.getParameter("category");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("todos", user.getTodos(category));
        request.setAttribute("categoryFilter", category);
        response.setContentType("text/html");

        try {
            RequestDispatcher view = request.getRequestDispatcher("todos.jsp");
            view.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
