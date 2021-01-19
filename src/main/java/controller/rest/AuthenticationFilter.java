package controller.rest;

import model.User;
import model.UserException;
import model.UserManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/api/*")
public class AuthenticationFilter extends HttpFilter {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletContext servletContext = getServletContext();
        String pathInfo = request.getServletPath();
        if(pathInfo != null && !pathInfo.isEmpty()){
            String[] requestPattern = pathInfo.split("/");
            if(requestPattern.length >= 3){
                String methodName = requestPattern[2];
                if("users".equals(methodName.toLowerCase())){
                    chain.doFilter(request,response);
                } else {
                    try {
                        String header = request.getHeader("Authorization");
                        String[] tokens = header.split(" ");
                        if (!tokens[0].equals("Basic")) {
                            throw new IllegalArgumentException();
                        }
                        byte[] decoded = Base64.getDecoder().decode(tokens[1]);
                        String[] credentials = new String(decoded).split(":");
                        int userID = validate(credentials, servletContext); // throws an exception if the credentials are invalid
                        if(userID >= 0){
                            request.setAttribute("userID", userID);
                            chain.doFilter(request, response);
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        LOGGER.warning(" - - - - Unauthorized : " + request.getPathInfo() + " - - - - ");
                    }
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.warning(" - - - - Resource not found : " + pathInfo + " - - - - ");
        }
    }
    private int validate(String[] credentials, ServletContext servletContext){
        UserManager userManager = UserManager.getInstance(servletContext);
        User tempUser;
        if(credentials.length >= 2){
            try{
                tempUser = userManager.authenticate(credentials[0], credentials[1]);
                return tempUser.getUserID();
            } catch (UserException exception){
                LOGGER.warning(" - - - - Unauthorized : " + exception.getMessage() + " - - - - ");
                return -1;
            }
        } else {
            LOGGER.warning(" - - - - Unauthorized : Wrong credential type - - - - ");
            return -1;
        }
    }
}
