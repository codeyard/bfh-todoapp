import java.time.LocalDate;

public class Test {

    public static void main(String[] args)  {
        UserManager userManager = new UserManager();

        try {
            User user = userManager.authenticate("sepp", "trütsch");
            System.out.println(user.getUserName());

            Task task1 = new Task("Weltherrschaft durch Ländler", "Work", LocalDate.of(2020, 12, 31));
            user.addTask(task1);
            Task task2 = new Task("Ländler rules", "Work", LocalDate.of(2020, 12, 31));
            user.addTask(task2);
            Task task3 = new Task("Ländler rules today", "Private", LocalDate.of(2020, 12, 31));
            user.addTask(task3);

            for (Task task : user.getTasks()) {
                System.out.println(task.getTitle());
            }
        } catch (UserException ex) {
            ex.printStackTrace();
        }
    }
}
