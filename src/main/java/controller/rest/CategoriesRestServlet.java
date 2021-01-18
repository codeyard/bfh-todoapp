package controller.rest;

import model.User;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/api/categories")
public class CategoriesRestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CategoriesRestServlet.class .getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String acceptType = request.getHeader("Accept");

        if (!acceptType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            LOGGER.warning(" - - - - Wrong content Type from Request: " + acceptType + " - - - - ");
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE); // unsupported accept type
            //TODO: 401 user not authorized to implement
        } else {
                ServletContext servletContext = getServletContext();
                UserManager userManager = UserManager.getInstance(servletContext);

                Set<String> categorySet = new TreeSet<>();
                for (User user : userManager.getUsers()) {
                    //TODO: to discuss, we now add double entrances if multiple users have the same categories
                    // is this wanted? -> In my opinion, this does not make sense, lets return a SET!
                    categorySet.addAll(user.getDistinctCategories());
                }
                List<String> categories = new ArrayList<>(categorySet);
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
