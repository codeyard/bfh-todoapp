package controller.rest;

import model.User;
import model.UserException;
import model.UserManager;
import model.helper.JsonHelper;
import model.helper.XmlHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet("/api/users")
public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getContentType();
        String body = request.getReader().lines()
                .reduce("", (String::concat));
        Map<?, ?> map = JsonHelper.readJsonData(body);
        String name = (String) map.get("name");
        String password = (String) map.get("password");
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        try {
            if (name != null && !name.isEmpty() && password != null && !password.isEmpty()) {
                User user = userManager.register(name, password);
                XmlHelper.writeXmlData(userManager, servletContext);
                response.setStatus(201);
            } else {
                response.setStatus(400);
            }
        } catch (UserException e) {
            response.setStatus(409);
        }
    }
}
