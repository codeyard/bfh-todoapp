package controller.rest;

import model.User;
import model.UserManager;
import controller.rest.helper.JsonHelper;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Returns all categories via REST API.
 * Listens to "/api/categories" path.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
@WebServlet("/api/categories")
public class CategoriesRestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CategoriesRestServlet.class.getName());

    /**
     * Get all categories.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException is thrown when the response couldn't be written
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String acceptType = request.getHeader("Accept");

        if (!acceptType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            LOGGER.warning(" - - - - Wrong content Type from Request: " + acceptType + " - - - - ");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); // unsupported accept type
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            User user = userManager.getUser((Integer) request.getAttribute("userID"));
            List<String> categories = new ArrayList<>(user.getDistinctCategories());
            String json = JsonHelper.writeCategoryJsonData(categories);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(JsonHelper.CONTENT_TYPE);
            response.setCharacterEncoding(JsonHelper.ENCODING);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            LOGGER.info(" - - - -  Response given - - - - ");
        }
    }
}
