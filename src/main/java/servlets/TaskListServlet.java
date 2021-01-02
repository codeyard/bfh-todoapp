package servlets;

import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/tasks")
public class TaskListServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("tasks", user.getTasks());
        try {
            RequestDispatcher view = request.getRequestDispatcher("tasks.jsp");
            view.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        String category = request.getParameter("category");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("tasks", user.filterByCategory(category));
        response.setContentType("text/html");

        try {
            RequestDispatcher view = request.getRequestDispatcher("tasks.jsp");
            view.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}
