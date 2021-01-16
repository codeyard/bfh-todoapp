import model.Todo;
import model.User;
import model.UserException;
import model.UserManager;
import model.helper.XmlHelper;

import java.io.IOException;
import java.time.LocalDate;

public class Test {

    public static void main(String[] args)  {
        UserManager userManager = UserManager.getInstance();

        try {
            User user = userManager.authenticate("sepp", "truetsch");
            System.out.println("User " + user.getUserName() + " successfully authenticated.");
            System.out.println(user);
            System.out.println();

            Todo todo1 = new Todo("asc", "Work", LocalDate.of(2020, 12, 27));
            user.addTodo(todo1);
            Todo todo2 = new Todo("asc", "Work", LocalDate.of(2020, 12, 31), true);
            user.addTodo(todo2);
            Todo todo3 = new Todo("ss", "Private", LocalDate.of(2020, 12, 04));
            user.addTodo(todo3);

            for (Todo todo : user.getTodos()) {
                //System.out.println("Todo " + todo.getTitle() + " is due " + todo.getDueDate());
                System.out.println(todo);
                System.out.println(todo.getTodoID());
            }
            System.out.println();


            System.out.println("Todo " + todo3.getTitle() + " is overdue " + todo3.isOverdue());
            System.out.println();

            System.out.println("Work category");

            user.filterByCategory("Work").stream().forEach(t ->
                    System.out.println(t.getTitle() + " "
                            + t.getCategory() + " "
                            + t.getDueDate()));

            //model.User newUser = userManager.register("sepp", "schacherseppli");

            //XmlHelper.writeXmlData(userManager);


        } catch (UserException ex) {
            ex.printStackTrace();
        }
    }
}
