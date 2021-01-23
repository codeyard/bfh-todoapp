package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The Todo class implements a Todo
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class Todo implements Comparable {
    private static int todoCounter;
    private Integer todoID;
    private Integer userID;
    private String title;
    private String category;
    private LocalDate dueDate;
    private boolean isImportant = false;
    private boolean isCompleted = false;

    /**
     * Constructs a todo.
     */
    public Todo() {
    }

    /**
     * Constructs a todo.
     * The todoCounter is incremented and used as the Todo ID.
     *
     * @param title the title that describes the todo
     */
    public Todo(String title) {
        this.todoID = todoCounter++;
        this.title = title;
    }

    /**
     * Constructs a todo.
     * The todoCounter is incremented and used as the Todo ID.
     *
     * @param title    the title of the todo
     * @param category an optional category
     * @param dueDate  an optional due date
     */
    public Todo(String title, String category, LocalDate dueDate) {
        this(title);
        this.category = category;
        this.dueDate = dueDate;
    }

    /**
     * Constructs a todo.
     * The todoCounter is incremented and used as the Todo ID.
     *
     * @param title       the title of the todo
     * @param category    an optional category
     * @param dueDate     an optional due date
     * @param isImportant an optional boolean flag indicating whether the todo is marked as important
     */
    public Todo(String title, String category, LocalDate dueDate, boolean isImportant) {
        this(title, category, dueDate);
        this.isImportant = isImportant;
    }

    /**
     * Constructs a todo.
     * The todoCounter is incremented and used as the Todo ID.
     *
     * @param title       the title of the todo
     * @param category    an optional category
     * @param dueDate     an optional due date
     * @param isImportant an optional boolean flag indicating whether the todo is marked as important
     * @param isCompleted an optional boolean flag indicating whether the todo is completed
     */
    public Todo(String title, String category, LocalDate dueDate, boolean isImportant, boolean isCompleted) {
        this(title, category, dueDate, isImportant);
        this.isCompleted = isCompleted;
    }

    public static void setTodoCounter(Integer counter) {
        todoCounter = counter;
    }

    public Integer getTodoID() {
        return todoID;
    }

    public void setUserID(int userID) {
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
     * Indicates whether the todo is overdue
     *
     * @return true if the todo is overdue, false otherwise
     */
    @JsonIgnore
    public boolean isOverdue() {
        return dueDate != null && (dueDate.compareTo(LocalDate.now()) < 0) && !isCompleted;
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     * <p>
     * The todos are sorted first by due date, then by title and finally by the id of the Todo objects.
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
            LocalDate otherDate = ((Todo) other).getDueDate();
            if (dueDate == null && otherDate == null) {
                return compareTitleOrTodoID((Todo) other);
            } else if (dueDate == null) {
                return 1;
            } else if (otherDate == null) {
                return -1;
            } else if (dueDate.compareTo(otherDate) == 0) {
                return compareTitleOrTodoID((Todo) other);
            }
            return dueDate.compareTo(otherDate);
        } catch (DateTimeException ex) {
            ex.printStackTrace();
            return compareTitleOrTodoID((Todo) other);
        }
    }

    /**
     * Compares this object with the specified object for order. The order is specified by the title or the id of the
     * Todo objects.
     *
     * @param other the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or
     * greater than the specified object
     */
    private int compareTitleOrTodoID(Todo other) {
        if (title.compareTo(other.getTitle()) == 0) {
            return todoID.compareTo(other.getTodoID());
        } else {
            return title.compareTo(other.getTitle());
        }
    }

    @Override
    public String toString() {
        return "Todo{" +
            "todoId='" + todoID + '\'' +
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
     *
     * @param other the reference object with which to compare
     * @return true if this object is the same as the o argument, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Todo todo = (Todo) other;
        return Objects.equals(todoID, todo.todoID);
    }

    /**
     * Returns a hash code value for the object
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(todoID);
    }
}
