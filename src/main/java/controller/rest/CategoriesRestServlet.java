package controller.rest;

import model.User;
import model.UserManager;
import model.helper.JsonHelper;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
                List<String> categories = new ArrayList<>();
                for (User user : userManager.getUsers()) {
                    //TODO: to discuss, we now add double entrances if multiple users have the same categories
                    // is this wanted?
                    categories.addAll(user.getDistinctCategories());
                }
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
