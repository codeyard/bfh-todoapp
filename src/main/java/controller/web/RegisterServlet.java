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
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher view;

        if (user != null) {
            response.reset();
            response.sendRedirect("todos");
            LOGGER.info(" - - - - User logged in  - - - - ");
        } else {
            try {
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
                LOGGER.info(" - - - - Loading /register - - - - ");
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
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
                LOGGER.info(" - - - - User registered   - - - - ");
            } catch (UserException e) {
                request.setAttribute("registerFailed", true);
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
                LOGGER.warning(" - - - - User registering failed because of user inputs   - - - - ");
            } catch (ServletException e) {
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
            }
        } else {
            try {
                request.setAttribute("registerFailed", true);
                view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
                LOGGER.warning(" - - - - User registering failed because of wrong user inputs   - - - - ");
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
            }
        }
    }

    /**
     * Verifies register data
     * @param name  username
     * @param firstPassword first Password entered by user
     * @param secondPassword second Password entered by user
     * @return a boolean after verifying register data data
     */
    private boolean isValidUserData(String name, String firstPassword, String secondPassword) {
        return name != null && !name.isEmpty()
            && firstPassword != null && !firstPassword.isEmpty()
            && secondPassword != null && !secondPassword.isEmpty()
            && firstPassword.equals(secondPassword);
    }


}
