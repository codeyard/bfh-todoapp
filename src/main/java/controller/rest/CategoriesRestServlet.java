package controller.rest;

import model.User;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/categories")
public class CategoriesRestServlet extends HttpServlet {
    private final static String CONTENT_TYPE = "application/json";
    private final static String ENCODING = "UTF-8";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(CONTENT_TYPE)) {
            response.setStatus(406); // unsupported accept type
            //TODO: 401 user not authorized to implement
        } else {
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                List<String> categories = new ArrayList<>();
                for (User user : userManager.getUsers()) {
                    categories.addAll(user.getDistinctCategories());
                }
                String json = JsonHelper.writeCategoryJsonData(categories);
                response.setStatus(200);
                response.setContentType(CONTENT_TYPE);
                response.setCharacterEncoding(ENCODING);
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
