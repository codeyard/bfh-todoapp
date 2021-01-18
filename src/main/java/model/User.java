/**
 * The User class implements a user with his set of Todos.
 */

package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.*;
import java.util.stream.Collectors;

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
     * When loading the Data.xml, the highest assigned userID is determined and stored in the
     * static userCounter variable. When adding new users, the userCounter is increased. This way
     * we can ensure that each user has an unique ID.
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
        return todos;
    }

    /**
     * Filters the list of Todos by a category.
     *
     * @param category the category to filter by
     * @return a filtered list of Todos which contains all todos whose category match the specified category
     */
    public List<Todo> getTodos(String category) {
        if (category != null && !category.isEmpty()) {
            return todos.stream().filter(t -> ((t.getCategory() != null && t.getCategory().equals(category)))).collect(Collectors.toList());
        } else {
            return todos;
        }
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
}
