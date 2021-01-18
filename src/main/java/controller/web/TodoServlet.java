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
import java.util.logging.Logger;

@WebServlet("/todo")
public class TodoServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TodoServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.reset();
            response.sendRedirect("login");
            LOGGER.info(" - - - - User not logged in  - - - - ");
        } else {
            RequestDispatcher view;
            try {
                String todoID = request.getParameter("todoID");
                Todo todo;
                boolean isNew = true;
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
                LOGGER.info(" - - - - User creating or updating todo  - - - - ");
            } catch (ServletException e) {
                e.printStackTrace();
                view = request.getRequestDispatcher("errors.jsp");
                view.forward(request, response);
                LOGGER.severe(" - - - - Error occurred: " + e.getMessage() + " - - - - ");
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
        Integer todoID = null;

        if (user == null) {
            response.reset();
            response.sendRedirect("login");
            LOGGER.info(" - - - - User not logged in  - - - - ");
        } else {
            RequestDispatcher view;
            ServletContext servletContext = getServletContext();
            UserManager userManager = UserManager.getInstance(servletContext);

            if (id != null && !id.isEmpty()) {
                todoID = Integer.parseInt(id);
            }
            // DeleteTodo if delete button was clicked
            if (isDeleteButtonPressed(deleteButton, todoID)) {
                try {
                    user.deleteTodo(user.getTodo(todoID));
                    XmlHelper.writeXmlData(userManager, servletContext);
                    response.sendRedirect("todos");
                    LOGGER.info(" - - - - User deleted todo  - - - - ");
                } catch (IOException ioException) {
                    view = request.getRequestDispatcher("errors.jsp");
                    view.forward(request, response);
                    LOGGER.severe(" - - - - Error occurred: " + ioException.getMessage() + " - - - - ");
                }
            } else {
                // Create or update newTodo
                boolean isNew = Boolean.parseBoolean(request.getParameter("isNew"));
                String dueDateStr = request.getParameter("dueDate");
                LocalDate dueDate;
                boolean isImportant = request.getParameter("isImportant") != null;
                boolean isCompleted = request.getParameter("isCompleted") != null;

                try {
                    dueDate = parseUserDate(dueDateStr);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                    session.setAttribute("dateError", true);
                    response.sendRedirect(request.getRequestURL().toString() + "?todoID=" + todoID);
                    LOGGER.warning(" - - - - Wrong user Date input  - - - - ");
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
                LOGGER.info(" - - - - User successfully created or updated todo  - - - - ");
            }
        }
    }

    /**
     * Adds new todo to user
     * @param title title of todo
     * @param category category of todo
     * @param user the user where the todo will be added
     * @param dueDate the due date of todo
     * @param isImportant the importance of todo
     */
    private void addNewTodo(String title, String category, User user, LocalDate dueDate, boolean isImportant) {
        Todo todo = new Todo(title, category, dueDate, isImportant);
        user.addTodo(todo);
    }

    /**
     * Updates existing todo
     * @param title title of todo
     * @param category cateogry of todo
     * @param user user where the todo should be updates
     * @param todoID id of the todo
     * @param dueDate due date of todo
     * @param isImportant importance of todo
     * @param isCompleted completion status of todo
     */
    private void updateExistingTodo(String title, String category, User user, Integer todoID, LocalDate dueDate, boolean isImportant, boolean isCompleted) {
        Todo todo = user.getTodo(todoID);
        todo.setTitle(title);
        todo.setCategory(category);
        todo.setDueDate(dueDate);
        todo.setImportant(isImportant);
        todo.setCompleted(isCompleted);

        user.updateTodo(todo);
    }

    /**
     * Parses date that was entered by user
     * @param dueDateStr user input string
     * @return parsed due date if successfull
     * @throws DateTimeParseException Exception is thrown if date couldn't be pared
     */

    private LocalDate parseUserDate(String dueDateStr) throws DateTimeParseException {
        LocalDate dueDate = null;
        if (dueDateStr != null && !dueDateStr.isEmpty()) {
            dueDate = LocalDate.parse(dueDateStr);
        }
        return dueDate;
    }

    /**
     * Checks if delete button is pressed
     * @param deleteButton value of delete button
     * @param todoID id of todo
     * @return a boolean if todoButton was pressed
     */
    private boolean isDeleteButtonPressed(String deleteButton, Integer todoID) {
        return todoID != null && deleteButton != null && deleteButton.equals("Delete");
    }

}
