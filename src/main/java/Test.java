import java.time.LocalDate;

public class Test {

    public static void main(String[] args)  {
        UserManager userManager = new UserManager();

        try {
            User user = userManager.authenticate("sepp", "trütsch");
            System.out.println("User " + user.getUserName() + " successfully authenticated.");

            Task task1 = new Task("Weltherrschaft", "Work", LocalDate.of(2020, 12, 27));
            user.addTask(task1);
            Task task2 = new Task("Silvesternacht", "Work", LocalDate.of(2020, 12, 31));
            user.addTask(task2);
            Task task3 = new Task("Freitag also gestern", "Private", LocalDate.of(2020, 12, 04));
            user.addTask(task3);
            Task task4 = new Task("Servlets für Todo Application schreiben");
            user.addTask(task4);
            Task task5 = new Task("Ein Task ohne DueDate");
            user.addTask(task5);

            for (Task task : user.getTasks()) {
                System.out.println("Task " + task.getTitle() + " is due " + task.getDueDate());
            }

            System.out.println("Task " + task3.getTitle() + " is overdue " + task3.isOverdue());

            //User newUser = userManager.register("sepp", "schacherseppli");

        } catch (UserException ex) {
            ex.printStackTrace();
        }
    }
}
