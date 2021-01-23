package controller.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Resets a user session.
 * Listens to "/logout" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());

    /**
     * Resets a user session.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException if unable to redirect to login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.reset();
        response.sendRedirect("login");
        LOGGER.info(" - - - - User logged out  - - - - ");
    }
}
