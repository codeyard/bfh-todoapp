/**
 * The User Manager is responsible for the registration and authentication of a user.
 */

package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import model.helper.XmlHelper;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

@JacksonXmlRootElement(localName = "root")
public class UserManager {

    private static Set<User> users = new HashSet<>();

    private static UserManager instance;

    private UserManager() {
    }

    //todo: check if UserManager needs to be thread safe
    public static UserManager getInstance(ServletContext servletContext) {
        try {
            if (UserManager.instance == null) {
                UserManager.instance = loadUsers(servletContext);
                setCounters();
            }
            return UserManager.instance;
        } catch (Exception e) {
            UserManager.instance = new UserManager();
            return UserManager.instance;
        }
    }

    private static void setCounters() {
        Integer highestUserID = 0;
        Integer highestTodoID = 0;

        for (User user : UserManager.instance.getUsers()) {
            if (highestUserID < user.getUserID()) {
                highestUserID = user.getUserID();
            }
            for (Todo todo : user.getTodos()) {
                if (highestTodoID < todo.getTodoID()) {
                    highestTodoID = todo.getTodoID();
                }
            }
        }

        User.setUserCounter(++highestUserID);
        Todo.setTodoCounter(++highestTodoID);
    }

    /**
     * Registers a new user.
     *
     * @param userName the userName
     * @param password the password
     * @return a User object if registering was successful
     * @throws UserException if userName is already registered
     */
    public User register(String userName, String password) throws UserException {
        if (!isRegistered(userName)) {
            User newUser = new User(userName, password);
            users.add(newUser);
            return newUser;
        } else {
            throw new UserException("Username is not unique!");
        }
    }

    /**
     * Authenticates a user.
     *
     * @param userName the userName
     * @param password the password
     * @return a User object if authentication was successful
     * @throws UserException if the user does not exist or the passwords did not match
     */
    public User authenticate(String userName, String password) throws UserException {
        if (!isRegistered(userName)) {
            throw new UserException("User does not exist!");
        } else {
            Iterator<User> iterator = users.iterator();
            while (iterator.hasNext()) {
                User thisUser = iterator.next();
                if (thisUser.getUserName().equalsIgnoreCase(userName) && thisUser.getPassword().equals(password)) {
                    return thisUser;
                }
            }
            throw new UserException("Password does not match!");
        }
    }

    /**
     * Indicates whether a userName is already registered.
     *
     * @param userName the userName to check
     * @return true if userName is already registered, false otherwise
     */
    private boolean isRegistered(String userName) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUserName().equalsIgnoreCase(userName)) {
                return true;
            }
        }
        return false;
    }

    @JacksonXmlElementWrapper(localName = "users")
    @JacksonXmlProperty(localName = "user")
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Loads a predefined set of users.
     */
    private static UserManager loadUsers(ServletContext servletContext) {
        //users.add(new User("admin", "1234"));
        return XmlHelper.readXmlData(servletContext);
    }
}
