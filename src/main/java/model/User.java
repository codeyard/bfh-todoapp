/**
 * The User class implements a user with his set of Tasks.
 */

package model;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private String userID;
    private String userName;
    private String password;
    private Set<Task> tasks = new TreeSet<>();

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
     * Gets the set of Tasks.
     * @return a set of Tasks.
     */
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a Task to the set of Tasks.
     * @param task the Task object to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Updates a Task
     * @param task the Task object to update in the set
     */
    public void updateTask(Task task) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task tempTask = iterator.next();
            if (tempTask.getTaskID().equals(task.getTaskID())) {
                tempTask.setCategory(task.getCategory());
                tempTask.setCompleted(task.isCompleted());
                tempTask.setDueDate(task.getDueDate());
                tempTask.setImportant(task.isImportant());
                tempTask.setTitle(task.getTitle());
                break;
            }
        }
    }

    /**
     * Deletes a Task from the set
     * @param task the Task object to remove from the set.
     */
    public void deleteTask(Task task) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task tempTask = iterator.next();
            if (tempTask.getTaskID().equals(task.getTaskID())) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Gets a task from the set
     * @param taskID the ID of the task.
     * @return a Task object
     */
    public Task getTask(String taskID) {
        Task task = tasks.stream().filter(t -> t.getTaskID().equals(taskID)).findFirst().get();
        if (task != null) {
            return task;
        }
        return null;

    }

    public Set<String> getDistinctCategories() {
        return tasks.stream().map(t -> t.getCategory()).collect(Collectors.toSet());
    }

    /**
     * Filters the set of Tasks by a category.
     * @param category the category to filter by
     * @return a filtered set of Tasks which contains all tasks whose category match the specified category
     */
    public Set<Task> filterByCategory(String category) {
        return tasks.stream().filter(t -> ((t.getCategory() != null && t.getCategory().equals(category)))).collect(Collectors.toSet());
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
            ", tasks=" + tasks +
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
