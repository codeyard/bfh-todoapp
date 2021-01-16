package controller.web;

import model.Todo;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/todo")
public class TodoServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null ) {
            response.reset();
            response.sendRedirect("login");
        } else {

            try {
                String todoID = request.getParameter("todoID");
                Todo todo;
                Boolean isNew = true;
                if (todoID != null && !todoID.isEmpty()) {
                    todo = user.getTodo(Integer.parseInt(todoID));
                    isNew = false;
                }else{
                    todo = new Todo();
                }
                request.setAttribute("todo", todo);
                request.setAttribute("isNew", isNew);

                RequestDispatcher view = request.getRequestDispatcher("todo.jsp");
                view.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Throw ServletException, IOException?

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("todoID");
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String newCategory = request.getParameter("newCategory");
        String deleteButton =request.getParameter("Delete");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        Integer todoID = null;
        if(id != null && !id.isEmpty()){
            todoID = Integer.parseInt(id);
        }

        if(todoID != null && deleteButton != null && deleteButton.equals( "Delete")) {
            try {
                user.deleteTodo(user.getTodo(todoID));
                response.sendRedirect("todos");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Boolean isNew = Boolean.parseBoolean(request.getParameter("isNew"));
            String dueDateStr = request.getParameter("dueDate");
            LocalDate dueDate = null;
            if (dueDateStr != null && !dueDateStr.isEmpty()) {
                dueDate = LocalDate.parse(dueDateStr);
            }
            Boolean isImportant = request.getParameter("isImportant") != null;
            Boolean isCompleted = request.getParameter("isCompleted") != null;

            if (newCategory != null && !newCategory.isEmpty()) {
                category = newCategory;
            }

            if (isNew && todoID == null) {
                Todo todo = new Todo(title, category, dueDate, isImportant);
                user.addTodo(todo);
            } else {
                Todo todo = user.getTodo(todoID);
                todo.setTitle(title);
                todo.setCategory(category);
                todo.setDueDate(dueDate);
                todo.setImportant(isImportant);
                todo.setCompleted(isCompleted);

                user.updateTodo(todo);
            }

            response.sendRedirect("todos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

    }
}
