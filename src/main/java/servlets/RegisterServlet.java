package servlets;

import model.User;
import model.UserException;
import model.UserManager;

import javax.servlet.RequestDispatcher;
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

        if (user != null ) {
            response.reset();
            response.sendRedirect("tasks");
        } else {
            try {
                RequestDispatcher view = request.getRequestDispatcher("register.html");
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
        if (firstPassword != null && !firstPassword.isEmpty() && secondPassword != null && !secondPassword.isEmpty() && firstPassword.equals(secondPassword)) {
            UserManager userManager = UserManager.getInstance();
            try {
                userManager.register(name, firstPassword);
                RequestDispatcher view = request.getRequestDispatcher("index.html");
                view.forward(request, response);
            } catch (UserException e) {
                e.printStackTrace();
                try (PrintWriter out = response.getWriter()) {
                    htmlHelper(false, out);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                htmlHelper(true, out);
            } catch (IOException exception) {
                exception.printStackTrace();
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
