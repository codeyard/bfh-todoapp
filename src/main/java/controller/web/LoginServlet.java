package controller.web;

import model.User;
import model.UserException;
import model.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        RequestDispatcher view;

        User user = (User) session.getAttribute("user");
        if (user != null) {
            response.reset();
            response.sendRedirect("todos");
        } else {
            try {
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                view = request.getRequestDispatcher("errors.jsp");
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("userName");
        String password = request.getParameter("password");
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        try {
            User user = userManager.authenticate(name, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("todos");
            }
        } catch (UserException e) {
            RequestDispatcher view;
            try {
                request.setAttribute("loginFailed", true);
                request.setAttribute("userName", name);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            } catch (IOException | ServletException exception) {
                exception.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
            }
        }
    }
}
