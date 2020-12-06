import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Task  implements Comparable{
    private String taskID;
    private String userID;
    private String title;
    private String category;
    private LocalDate dueDate;
    private boolean isImportant = false;
    private boolean isCompleted = false;

    public Task(String title){
        this.taskID = UUID.randomUUID().toString();
        this.title = title;
    }

    public Task(String title, String category, LocalDate dueDate) {
        this(title);
        this.category = category;
        this.dueDate = dueDate;
    }

    public Task(String title, String category, LocalDate dueDate, boolean isImportant) {
        this(title, category, dueDate);
        this.isImportant = isImportant;
    }

    public String getTaskID(){
        return taskID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isOverdue(){
        return (dueDate.compareTo(LocalDate.now()) < 0) && !isCompleted;
    }

    @Override
    public int compareTo(Object other) {
        try {
            if (this.equals(other)) {
                return 0;
            }
            LocalDate otherDate = ((Task) other).getDueDate();
            if(dueDate == null && otherDate == null)  {
                return compareTitleOrTaskID((Task) other);
            }
            if (dueDate == null && otherDate != null) {
                return 1;
            }
            if(dueDate.compareTo(otherDate) == 0) {
                return compareTitleOrTaskID((Task) other);
            }
            return dueDate.compareTo(otherDate);
        } catch (DateTimeException ex) {
            ex.printStackTrace();
            return compareTitleOrTaskID((Task) other);
        }

        /**
        try {
            if (this.equals(other)) {
                return 0;
            } else {
                LocalDate taskDate = ((Task) other).getDueDate();
                if (dueDate != null) {
                    if (dueDate.compareTo(taskDate) == 0) {
                        return compareTitleOrTaskID((Task) other);
                    } else {
                        return dueDate.compareTo(taskDate);
                    }
                } else {
                    return 1;
                }
            }
        } catch (DateTimeException ex) {
            ex.printStackTrace();
            return compareTitleOrTaskID((Task) other);
        }
         */
    }

    private int compareTitleOrTaskID(Task other) {
        if (title.compareTo(other.getTitle()) == 0) {
            return taskID.compareTo(other.getTaskID());
        } else {
            return title.compareTo(other.getTitle());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskID, task.taskID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }
}
