package model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import model.helper.XmlHelper;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

@JacksonXmlRootElement(localName = "root")
public class UserManager {

    private static final Set<User> users = new HashSet<>();

    private static UserManager instance;

    private UserManager() {
    }

    //todo: check if UserManager needs to be thread safe -> WE DON'T HAVE ANY THREADS BY NOW
    public static UserManager getInstance(ServletContext servletContext) {
        try {
            if (UserManager.instance == null) {
                UserManager.instance = loadUsers(servletContext);
                //TODO: check where we should use setCounters(), right now the counters are not set correctly if
                //you only use the REST API and do not interact with the GUI -> EDIT: FOR ME IT WORKS CORRECTLY!! (IGOR)
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
            for (User thisUser : users) {
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
        for (User user : users) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
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
