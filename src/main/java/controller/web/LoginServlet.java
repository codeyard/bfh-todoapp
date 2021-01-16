package controller.web;

import model.User;
import model.UserException;
import model.UserManager;

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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

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
                RequestDispatcher view = request.getRequestDispatcher("index.html");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {


        String name = request.getParameter("userName");
        String password = request.getParameter("password");
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        try {
            User user = userManager.authenticate(name, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("todos");
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
