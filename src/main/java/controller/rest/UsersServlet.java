package controller.rest;

import model.UserException;
import model.UserManager;
import model.helper.JsonHelper;
import model.helper.XmlHelper;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@WebServlet("/api/users")
public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase("application/json")) {
            response.setStatus(415); // unsupported content type
        } else {
            try {
                String body = request.getReader()
                    .lines()
                    .reduce("", (String::concat));
                Map<String, String> map = JsonHelper.readJsonData(body);
                String name = map.get("name");
                String password = map.get("password");
                ServletContext servletContext = getServletContext();
                UserManager userManager = UserManager.getInstance(servletContext);
                try {
                    if (name != null && !name.isEmpty() && password != null && !password.isEmpty()) {
                        userManager.register(name, password);
                        XmlHelper.writeXmlData(userManager, servletContext);
                        response.setStatus(201); // user registered
                    } else {
                        response.setStatus(400); // invalid user data
                    }
                } catch (UserException e) {
                    response.setStatus(409); // a user with the same name already exists
                }
            } catch (Exception e) {
                response.setStatus(400); // invalid user data
            }
        }
    }
}
