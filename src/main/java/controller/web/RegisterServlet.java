package controller.web;

import model.User;
import model.UserException;
import model.UserManager;
import model.helper.XmlHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            response.reset();
            response.sendRedirect("todos");
        } else {
            try {
                RequestDispatcher view = request.getRequestDispatcher("register.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("userName");
        String firstPassword = request.getParameter("firstPassword");
        String secondPassword = request.getParameter("secondPassword");
        ServletContext servletContext = getServletContext();
        if (name != null && !name.isEmpty()
            && firstPassword != null && !firstPassword.isEmpty()
            && secondPassword != null && !secondPassword.isEmpty()
            && firstPassword.equals(secondPassword)) {
            UserManager userManager = UserManager.getInstance(servletContext);
            try {
                userManager.register(name, firstPassword);
                XmlHelper.writeXmlData(userManager, servletContext);
                RequestDispatcher view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
            } catch (UserException | ServletException | IOException e) {
                request.setAttribute("registerFailed", true);
                RequestDispatcher view = request.getRequestDispatcher("register.jsp");
                try {
                    view.forward(request, response);
                } catch (ServletException servletException) {
                    servletException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        } else {
            request.setAttribute("registerFailed", true);
            RequestDispatcher view = request.getRequestDispatcher("register.jsp");
            try {
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void htmlHelper(boolean isPasswordInvalid, PrintWriter out) {
        out.println("<html><body>");
        if (isPasswordInvalid) {
            out.println(" <h1>Invalid password or not the same password</h1>");
        } else {
            out.println(" <h1>Username already exists</h1>");
        }
        out.println(" <a href='login'>Back</a>");
        out.println("</body></html>");
    }
}
