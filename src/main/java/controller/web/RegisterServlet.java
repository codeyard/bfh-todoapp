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
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            response.reset();
            response.sendRedirect("todos");
        } else {
            try {
                RequestDispatcher view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("userName");
        String firstPassword = request.getParameter("firstPassword");
        String secondPassword = request.getParameter("secondPassword");
        request.setAttribute("userName", name);
        ServletContext servletContext = getServletContext();
        if (firstPassword != null && !firstPassword.isEmpty() && secondPassword != null && !secondPassword.isEmpty() && firstPassword.equals(secondPassword)) {
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                userManager.register(name, firstPassword);
                XmlHelper.writeXmlData(userManager, servletContext);
                RequestDispatcher view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            } catch (UserException e) {
                e.printStackTrace();
                try (PrintWriter out = response.getWriter()) {
                    request.setAttribute("errorMessage", "The desired username is already taken!");
                    RequestDispatcher view = request.getRequestDispatcher("register.jsp");
                    view.forward(request, response);
                } catch (IOException | ServletException exception) {
                    exception.printStackTrace();
                }
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                request.setAttribute("errorMessage", "The Passwords do not match or are invalid!");
                RequestDispatcher view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (IOException | ServletException exception) {
                exception.printStackTrace();
            }
        }
    }
}
