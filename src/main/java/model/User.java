package model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class User {
    private String userID;
    private String userName;
    private String password;
    private Set<Task> tasks = new TreeSet<>();

    public User(String userName, String password) {
        this.userID =  UUID.randomUUID().toString();
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void updateTask(Task task){
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()) {
            Task tempTask = iterator.next();
            if(tempTask.getTaskID().equals(task.getTaskID())){
                tempTask.setCategory(task.getCategory());
                tempTask.setCompleted(task.isCompleted());
                tempTask.setDueDate(task.getDueDate());
                tempTask.setImportant(task.isImportant());
                tempTask.setTitle(task.getTitle());
                break;
            }
        }
    }

    public void deleteTask(Task task){
        Iterator<Task> iterator = tasks.iterator();
        while(iterator.hasNext()) {
            Task tempTask = iterator.next();
            if(tempTask.getTaskID().equals(task.getTaskID())){
                iterator.remove();
                break;
            }
        }
    }
}
