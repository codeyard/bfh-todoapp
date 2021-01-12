/**
 * The User class implements a user with his set of Todos.
 */

package model;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private String userID;
    private String userName;
    private String password;
    private Set<Todo> todos = new TreeSet<>();

    /**
     * Constructs a user.
     * A UUID is generated and used as the User ID.
     * @param userName the userName
     * @param password the password
     */
    public User(String userName, String password) {
        this.userID = UUID.randomUUID().toString();
        this.userName = userName;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Gets the set of Todos.
     * @return a set of Todos.
     */
    public Set<Todo> getTodos() {
        return todos;
    }

    public void setTodos(Set<Todo> todos) {
        this.todos = todos;
    }

    /**
     * Adds a Todo to the set of Todos.
     * @param todo the Todo object to add
     */
    public void addTodo(Todo todo) {
        todos.add(todo);
    }

    /**
     * Updates a Todo
     * @param todo the Todo object to update in the set
     */
    public void updateTodo(Todo todo) {
        Iterator<Todo> iterator = todos.iterator();
        while (iterator.hasNext()) {
            Todo tempTodo = iterator.next();
            if (tempTodo.getTodoID().equals(todo.getTodoID())) {
                tempTodo.setCategory(todo.getCategory());
                tempTodo.setCompleted(todo.isCompleted());
                tempTodo.setDueDate(todo.getDueDate());
                tempTodo.setImportant(todo.isImportant());
                tempTodo.setTitle(todo.getTitle());
                break;
            }
        }
    }

    /**
     * Deletes a Todo from the set
     * @param todo the Todo object to remove from the set.
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
    }

    /**
     * Gets a todo from the set
     * @param todoID the ID of the todo.
     * @return a Todo object
     */
    public Todo getTodo(String todoID) {
        Todo todo = todos.stream().filter(t -> t.getTodoID().equals(todoID)).findFirst().get();
        if (todo != null) {
            return todo;
        }
        return null;

    }

    public Set<String> getDistinctCategories() {
        return todos.stream().map(t -> t.getCategory()).collect(Collectors.toSet());
    }

    /**
     * Filters the set of Todos by a category.
     * @param category the category to filter by
     * @return a filtered set of Todos which contains all todos whose category match the specified category
     */
    public Set<Todo> filterByCategory(String category) {
        return todos.stream().filter(t -> ((t.getCategory() != null && t.getCategory().equals(category)))).collect(Collectors.toSet());
    }

    /**
     * Returns a string representation of the User object.
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
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }
}
