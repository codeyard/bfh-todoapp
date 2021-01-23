package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The User class implements a user with his set of Todos.
 */
@JacksonXmlRootElement(localName = "user")
public class User {
    private static int userCounter;
    private Integer userID;
    private String userName;
    private String password;
    private List<Todo> todos = new ArrayList<>();

    public User() {
    }

    /**
     * Constructs a user.
     * The userCounter is incremented and used as the User ID.
     *
     * @param userName the userName
     * @param password the password
     */
    public User(String userName, String password) {
        this.userID = userCounter++;
        this.userName = userName;
        this.password = password;
    }

    public Integer getUserID() {
        return userID;
    }

    /**
     * Sets the userCounter to the defined value
     *
     * @param counter the counter that holds the highest assigned userID
     */
    public static void setUserCounter(Integer counter) {
        userCounter = counter;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Gets the list of Todos.
     *
     * @return a list of Todos.
     */
    @JacksonXmlElementWrapper(localName = "todos")
    @JacksonXmlProperty(localName = "todo")
    public List<Todo> getTodos() {
        return getTodos(null, null);
    }

    /**
     * Filters the list of Todos by a category.
     *
     * @param category the category to filter by
     * @return a filtered list of Todos which contains all todos whose category match the specified category
     */
    public List<Todo> getTodos(String category) {
        return getTodos(category, null);
    }

    /**
     * Filters the list of Todos by a category and/or a status
     * @param category the category to filter by
     * @param status the status to filter by. Valid values are complete, incomplete, overdue and important.
     * @return a filtered list of Todos which contains all todos whose category match the specified category and/or
     * whose status match the specified status
     */
    public List<Todo> getTodos(String category, String status) {
        Predicate<Todo> filter = (t -> true);
        if (category != null && !category.isEmpty()) {
            filter = filter.and(t -> t.getCategory() != null && t.getCategory().equals(category));
        }
        if (status != null && !status.isEmpty()) {
            switch (status.toLowerCase()) {
                case "complete":
                    filter = filter.and(Todo::isCompleted);
                    break;
                case "incomplete":
                    filter = filter.and(t -> !t.isCompleted());
                    break;
                case "overdue":
                    filter = filter.and(Todo::isOverdue);
                    break;
                case "important":
                    filter = filter.and(Todo::isImportant);
                    break;
                default:
            }
        }
        return todos.stream().filter(filter).collect(Collectors.toList());
    }

    /**
     * Generates a string with todo statistics if a user has more than one todo.
     *
     * @return a string with todo statistics
     */
    @JsonIgnore
    public String getTodosStatistics(String category, String status) {
        String stats = "";
        List<Todo> src = getTodos(category, status);
        long todosCount = src.size();
        if (todosCount > 1) {
            long openCount = src.stream().filter(t -> !t.isCompleted()).count();
            long importantCount = src.stream().filter(Todo::isImportant).count();
            long overdueCount = src.stream().filter(Todo::isOverdue).count();

            stats = "You have " + todosCount + " todos";
            stats += (openCount > 0 || importantCount > 0 || overdueCount > 0) ? ": " : "";
            if (openCount > 0) {
                stats += (openCount > 1) ? openCount + " are still incomplete" : "one is still incomplete";
            } else {
                stats += " all are complete";
            }
            stats += (importantCount > 0 || overdueCount > 0) ? ", " : "";
            if (importantCount > 0) {
                stats += (importantCount > 1) ? importantCount + " are important" : "one is important";
            }
            stats += (overdueCount > 0) ? ", " : "";
            if (overdueCount > 0) {
                stats += (overdueCount > 1) ? overdueCount + " are overdue" : "one is overdue";
            }
            stats += ".";
        } else if (todosCount == 0) {
            if ((category != null && !category.isEmpty()) || (status != null && !status.isEmpty())) {
                stats = "You have no todos matching the selected criteria.";
            }
        }
        return stats;
    }

    /**
     * Adds a Todo to the list of Todos.
     *
     * @param todo the Todo object to add
     */
    public void addTodo(Todo todo) {
        todo.setUserID(userID);
        if (todos == null) {
            todos = new ArrayList<>();
        }
        todos.add(todo);
        Collections.sort(todos);
    }

    /**
     * Updates a Todo
     *
     * @param todo the Todo object to update in the list
     */
    public void updateTodo(Todo todo) {
        for (Todo tempTodo : todos) {
            if (tempTodo.getTodoID().equals(todo.getTodoID())) {
                tempTodo.setCategory(todo.getCategory());
                tempTodo.setCompleted(todo.isCompleted());
                tempTodo.setDueDate(todo.getDueDate());
                tempTodo.setImportant(todo.isImportant());
                tempTodo.setTitle(todo.getTitle());
                break;
            }
        }
        Collections.sort(todos);
    }

    /**
     * Deletes a Todo from the list
     *
     * @param todo the Todo object to remove from the list.
     */
    public void deleteTodo(Todo todo) {
        Iterator<Todo> iterator = todos.iterator();
        while (iterator.hasNext()) {
            Todo tempTodo = iterator.next();
            if (tempTodo.getTodoID().equals(todo.getTodoID())) {
                iterator.remove();
                break;
            }
        }
        Collections.sort(todos);
    }

    /**
     * Gets a todo from the list
     *
     * @param todoID the ID of the todo.
     * @return a Todo object
     */
    public Todo getTodo(Integer todoID) {
        Optional<Todo> todo = todos.stream().filter(t -> t.getTodoID().equals(todoID)).findFirst();
        return todo.orElse(null);

    }

    /**
     * Returns a set with all distinct categories.
     *
     * @return a set with all distinct categories
     */
    @JsonIgnore
    public Set<String> getDistinctCategories() {
        return todos.stream().map(Todo::getCategory).filter(x -> !x.isEmpty()).collect(Collectors.toSet());
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return a string representation of the User object
     */
    @Override
    public String toString() {
        return "User{" +
            "userID='" + userID + '\'' +
            ", userName='" + userName + '\'' +
            ", todos=" + todos +
            '}';
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the o argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userID, user.userID);
    }

    /**
     * Returns a hash code value for the object
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

    /**
     * Generates a list of Todos which are completed or not
     * @param isCompleted a boolean which decides if a list with all completed or incompleted todos is returned
     * @return a list of Todo which are either completed or not
     */
    @JsonIgnore
    public List<Todo> getCompletetOrIncompletedTodos(boolean isCompleted){
        return todos.stream().filter(t -> (t.isCompleted() == isCompleted)).collect(Collectors.toList());
    }

    /**
     * Generates a list of all overdue Todos
     * @return a list of Todos which are overdue
     */
    @JsonIgnore
    public List<Todo> getAllOverdueTodos(){
        return todos.stream().filter(t -> (t.isOverdue() == true)).collect(Collectors.toList());
    }
}
