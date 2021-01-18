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
public class UsersRestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(JsonHelper.STATUS_415); // unsupported content type
        } else {
            try {
                String body = request.getReader()
                        .lines()
                        .reduce("", (String::concat));
                Map<String, ?> map = JsonHelper.readJsonData(body);
                if (map != null && !map.isEmpty()) {
                    String name = (String) map.get("name");
                    String password = (String) map.get("password");
                    ServletContext servletContext = getServletContext();
                    UserManager userManager = UserManager.getInstance(servletContext);
                    try {
                        if (name != null && !name.isEmpty() && password != null && !password.isEmpty()) {
                            userManager.register(name, password);
                            XmlHelper.writeXmlData(userManager, servletContext);
                            response.setStatus(JsonHelper.STATUS_201); // user registered
                        } else {
                            response.setStatus(JsonHelper.STATUS_400); // invalid user data
                        }
                    } catch (UserException e) {
                        response.setStatus(JsonHelper.STATUS_409); // a user with the same name already exists
                    }
                } else {
                    response.setStatus(JsonHelper.STATUS_400); // invalid user data
                }
            } catch (Exception e) {
                response.setStatus(JsonHelper.STATUS_400); // invalid user data
            }
        }
    }
}
