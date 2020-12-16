/**
 * The Task class implements a Task
 */

package model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Task implements Comparable {
    private String taskID;
    private String userID;
    private String title;
    private String category;
    private LocalDate dueDate;
    private boolean isImportant = false;
    private boolean isCompleted = false;

    /**
     * Constructs a task.
     * A UUID is generated and used as the User ID.
     * @param title the title of the task
     */
    public Task(String title) {
        this.taskID = UUID.randomUUID().toString();
        this.title = title;
    }

    /**
     * Constructs a task.
     * A UUID is generated and used as the User ID.
     * @param title the title of the task
     * @param category an optional category
     * @param dueDate an optional due date
     */
    public Task(String title, String category, LocalDate dueDate) {
        this(title);
        this.category = category;
        this.dueDate = dueDate;
    }

    /**
     * Constructs a task.
     * A UUID is generated and used as the User ID.
     * @param title the title of the task
     * @param category an optional category
     * @param dueDate an optional due date
     * @param isImportant an optional boolean flag indicating whether the task is marked as important
     */
    public Task(String title, String category, LocalDate dueDate, boolean isImportant) {
        this(title, category, dueDate);
        this.isImportant = isImportant;
    }

    public String getTaskID() {
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

    /**
     * Indicates whether the task is overdue
     * @return true if the task is overdue, false otherwise
     */
    public boolean isOverdue() {
        return dueDate != null && (dueDate.compareTo(LocalDate.now()) < 0) && !isCompleted;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     *
     * The tasks are sorted first by due date, then by title and finally by the id of the Task objects.
     *
     * @param other the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or
     * greater than the specified object
     */
    @Override
    public int compareTo(Object other) {
        try {
            if (this.equals(other)) {
                return 0;
            }
            LocalDate otherDate = ((Task) other).getDueDate();
            if (dueDate == null && otherDate == null) {
                return compareTitleOrTaskID((Task) other);
            }
            if (dueDate == null && otherDate != null) {
                return 1;
            }
            if (dueDate.compareTo(otherDate) == 0) {
                return compareTitleOrTaskID((Task) other);
            }
            return dueDate.compareTo(otherDate);
        } catch (DateTimeException ex) {
            ex.printStackTrace();
            return compareTitleOrTaskID((Task) other);
        }
    }

    /**
     * Compares this object with the specified object for order. The order is specified by the title or the id of the
     * Task objects.
     * @param other the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or
     * greater than the specified object
     */
    private int compareTitleOrTaskID(Task other) {
        if (title.compareTo(other.getTitle()) == 0) {
            return taskID.compareTo(other.getTaskID());
        } else {
            return title.compareTo(other.getTitle());
        }
    }

    /**
     * Returns a string representation of the Task object.
     * @return a string representation of the Task object
     */
    @Override
    public String toString() {
        return "Task{" +
            "taskID='" + taskID + '\'' +
            ", userID='" + userID + '\'' +
            ", title='" + title + '\'' +
            ", category='" + category + '\'' +
            ", dueDate=" + dueDate +
            ", isImportant=" + isImportant +
            ", isCompleted=" + isCompleted +
            ", isOverdue=" + isOverdue() +
            '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskID, task.taskID);
    }

    /**
     * Returns a hash code value for the object
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskID);
    }
}
