import model.Task;
import model.User;
import model.UserException;
import model.UserManager;

import java.time.LocalDate;
import java.util.Set;

public class Test {

    public static void main(String[] args)  {
        UserManager userManager = UserManager.getInstance();

        try {
            User user = userManager.authenticate("sepp", "trütsch");
            System.out.println("User " + user.getUserName() + " successfully authenticated.");
            System.out.println(user);
            System.out.println();

            Task task1 = new Task("Weltherrschaft", "Work", LocalDate.of(2020, 12, 27));
            user.addTask(task1);
            Task task2 = new Task("Silvesternacht", "Work", LocalDate.of(2020, 12, 31), true);
            user.addTask(task2);
            Task task3 = new Task("Vergangenheit", "Private", LocalDate.of(2020, 12, 04));
            user.addTask(task3);
            Task task4 = new Task("Servlets für Todo Application schreiben");
            user.addTask(task4);
            Task task5 = new Task("Ein Task ohne DueDate");
            user.addTask(task5);

            for (Task task : user.getTasks()) {
                //System.out.println("Task " + task.getTitle() + " is due " + task.getDueDate());
                System.out.println(task);
            }
            System.out.println();


            System.out.println("Task " + task3.getTitle() + " is overdue " + task3.isOverdue());
            System.out.println();

            System.out.println("Work category");

            user.filterByCategory("Work").stream().forEach(t ->
                    System.out.println(t.getTitle() + " "
                            + t.getCategory() + " "
                            + t.getDueDate()));

            //model.User newUser = userManager.register("sepp", "schacherseppli");

        } catch (UserException ex) {
            ex.printStackTrace();
        }
    }
}
