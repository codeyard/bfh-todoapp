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
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;


@WebServlet("/api/users")
public class UsersRestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UsersRestServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String contentType = request.getContentType();
        if (!contentType.equalsIgnoreCase(JsonHelper.CONTENT_TYPE)) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE); // unsupported content type
            LOGGER.warning(" - - - - Wrong content Type from Request: " + contentType + " - - - - ");

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
                            response.setStatus(HttpServletResponse.SC_CREATED); // user registered
                            LOGGER.info(" - - - -  Response given - - - - ");
                        } else {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // invalid user data
                            LOGGER.warning(" - - - - Resource not found: " + request.getPathInfo() + " - - - - ");
                        }
                    } catch (UserException e) {
                        response.setStatus(HttpServletResponse.SC_CONFLICT); // a user with the same name already exists
                        LOGGER.warning(" - - - - user with same name exists  - - - - ");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // invalid user data
                    LOGGER.warning(" - - - - Invalid user data  - - - - ");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // invalid user data
                LOGGER.warning(" - - - - Invalid user data  - - - - ");
            }
        }
    }
}
