package servlets;

import model.Task;
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

@WebServlet("/task")
public class TaskServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try {
            String taskID = request.getParameter("taskID");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            Task task = new Task();
            Boolean isNew = true;
            if (taskID != null && !taskID.isEmpty()) {
                task = user.getTask(taskID);
                isNew = false;
            }
            request.setAttribute("task", task);
            request.setAttribute("isNew", isNew);

            RequestDispatcher view = request.getRequestDispatcher("task.jsp");
            view.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    // TODO: Throw ServletException, IOException?

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String taskID = request.getParameter("taskID");
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String deleteButton =request.getParameter("Delete");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if(deleteButton != null && deleteButton.equals( "Delete")) {
            try {
                user.deleteTask(user.getTask(taskID));
                response.sendRedirect("tasks");
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



            if (isNew) {
                Task task = new Task(title, category, dueDate, isImportant);
                user.addTask(task);
            } else {
                Task task = user.getTask(taskID);
                task.setTitle(title);
                task.setCategory(category);
                task.setDueDate(dueDate);
                task.setImportant(isImportant);
                task.setCompleted(isCompleted);

                user.updateTask(task);
            }

            response.sendRedirect("tasks");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {

    }
}
