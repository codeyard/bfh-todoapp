import java.util.Date;
import java.util.UUID;

public class Task  implements Comparable{
    private String taskID;
    private String userID; //todo: needed??
    private String title;
    private String category;
    private Date dueDate;
    private boolean isImportant = false;
    private boolean isCompleted = false;

    public Task(String title){
        this.taskID = UUID.randomUUID().toString();
        this.title = title;
    }

    public Task(String title, String category, Date dueDate) {
        this(title);
        this.category = category;
        this.dueDate = dueDate;
    }

    public Task(String title, String category, Date dueDate, boolean isImportant) {
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
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
        return (dueDate.compareTo(new Date()) < 0) && !isCompleted;
    }

    @Override
    public int compareTo(Object task) {
        if(task instanceof Task){
            return ((Task) task).getDueDate().compareTo(dueDate);
        }else{
            return 0;
        }
    }
}
