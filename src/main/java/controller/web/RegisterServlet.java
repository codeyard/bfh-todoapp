package controller.web;

import model.User;
import model.UserException;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher view;

        if (user != null) {
            response.reset();
            response.sendRedirect("todos");
        } else {
            try {
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("userName");
        String firstPassword = request.getParameter("firstPassword");
        String secondPassword = request.getParameter("secondPassword");
        ServletContext servletContext = getServletContext();
        RequestDispatcher view;
        if (isValidUserData(name, firstPassword, secondPassword)) {
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                userManager.register(name, firstPassword);
                XmlHelper.writeXmlData(userManager, servletContext);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            } catch (UserException e) {
                request.setAttribute("registerFailed", true);
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
            }
        } else {
            try {
                request.setAttribute("registerFailed", true);
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
            }
        }
    }

    private boolean isValidUserData(String name, String firstPassword, String secondPassword) {
        return name != null && !name.isEmpty()
                && firstPassword != null && !firstPassword.isEmpty()
                && secondPassword != null && !secondPassword.isEmpty()
                && firstPassword.equals(secondPassword);
    }


}
