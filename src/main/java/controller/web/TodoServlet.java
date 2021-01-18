package controller.web;

import model.Todo;
import model.User;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/todo")
public class TodoServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.reset();
            response.sendRedirect("login");
        } else {
            RequestDispatcher view;
            try {
                String todoID = request.getParameter("todoID");
                Todo todo;
                Boolean isNew = true;
                if (todoID != null && !todoID.isEmpty()) {
                    todo = user.getTodo(Integer.parseInt(todoID));
                    isNew = false;
                } else {
                    todo = new Todo();
                }
                boolean dateError = session.getAttribute("dateError") != null;
                request.setAttribute("todo", todo);
                request.setAttribute("isNew", isNew);
                request.setAttribute("dateError", dateError);


                view = request.getRequestDispatcher("todo.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
            }
        }
    }

    // TODO: Throw ServletException, IOException? -> Ich denke jetzt brauchen wir sie wegen der Error Seite!

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("todoID");
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String newCategory = request.getParameter("newCategory");
        String deleteButton = request.getParameter("Delete");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestDispatcher view;
        ServletContext servletContext = getServletContext();
        UserManager userManager = UserManager.getInstance(servletContext);
        Integer todoID = null;

        if (id != null && !id.isEmpty()) {
            todoID = Integer.parseInt(id);
        }
        // DeleteTodo if delete button was clicked
        if (isDeleteButtonPressed(deleteButton, todoID)) {
            try {
                user.deleteTodo(user.getTodo(todoID));
                XmlHelper.writeXmlData(userManager, servletContext);
                response.sendRedirect("todos");
            } catch (IOException ioException) {
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
            }
        } else {
            // Create or update newTodo
            boolean isNew = Boolean.parseBoolean(request.getParameter("isNew"));
            String dueDateStr = request.getParameter("dueDate");
            LocalDate dueDate = null;
            boolean isImportant = request.getParameter("isImportant") != null;
            boolean isCompleted = request.getParameter("isCompleted") != null;

            try {
                dueDate = parseUserDate(dueDateStr);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
                session.setAttribute("dateError", true);
                response.sendRedirect(request.getRequestURL().toString() + "?todoID=" + todoID);
                return;
            }


            if (newCategory != null && !newCategory.isEmpty()) {
                category = newCategory;
            }
            // Create newTodo
            if (isNew && todoID == null) {
                addNewTodo(title, category, user, dueDate, isImportant);
            }
            // Update existingTodo
            else {
                updateExistingTodo(title, category, user, todoID, dueDate, isImportant, isCompleted);
            }

            XmlHelper.writeXmlData(userManager, servletContext);
            response.sendRedirect("todos");
        }
    }

    private void addNewTodo(String title, String category, User user, LocalDate dueDate, boolean isImportant) {
        Todo todo = new Todo(title, category, dueDate, isImportant);
        user.addTodo(todo);
    }

    private void updateExistingTodo(String title, String category, User user, Integer todoID, LocalDate dueDate, boolean isImportant, boolean isCompleted) {
        Todo todo = user.getTodo(todoID);
        todo.setTitle(title);
        todo.setCategory(category);
        todo.setDueDate(dueDate);
        todo.setImportant(isImportant);
        todo.setCompleted(isCompleted);

        user.updateTodo(todo);
    }

    private LocalDate parseUserDate(String dueDateStr) throws DateTimeParseException {
        LocalDate dueDate = null;
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            dueDate = LocalDate.parse(dueDateStr);
        }
        return dueDate;
    }

    private boolean isDeleteButtonPressed(String deleteButton, Integer todoID) {
        return todoID != null && deleteButton != null && deleteButton.equals("Delete");
    }

}
