package controller.rest;

import model.Todo;
import model.User;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/api/categories")
public class CategoriesRestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(JsonHelper.STATUS_406); // unsupported accept type
            //TODO: 401 user not authorized to implement
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                //TODO: to discuss, we now add double entrances if multiple users have the same categories
                // is this wanted? -> In my opinion, this does not make sense, lets return a SET!
                Set<String> categorySet = userManager.getUsers().stream()
                        .flatMap(users -> users.getDistinctCategories().stream())
                        .collect(Collectors.toSet());

                List<String> categories = new ArrayList<>(categorySet);
                String json = JsonHelper.writeCategoryJsonData(categories);
                response.setStatus(JsonHelper.STATUS_200);
                response.setContentType(JsonHelper.CONTENT_TYPE);
                response.setCharacterEncoding(JsonHelper.ENCODING);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
