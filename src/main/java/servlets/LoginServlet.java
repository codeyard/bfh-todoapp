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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        try {
            RequestDispatcher view = request.getRequestDispatcher("index.html");
            view.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String name = request.getParameter("userName");
        String password = request.getParameter("password");
        UserManager userManager = UserManager.getInstance();
        try {
            User user = userManager.authenticate(name, password);
            if(user != null){
                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                response.sendRedirect("tasks");
            }
        } catch (UserException e) {
            try (PrintWriter out = response.getWriter()) {
                htmlHelper(e.getMessage(), out);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void htmlHelper(String message, PrintWriter out) {
        out.println("<html><body>");
        out.println(" <h1>" + message + "</h1>");
        out.println(" <a href='login'>Back</a>");
        out.println("</body></html>");
    }
}
