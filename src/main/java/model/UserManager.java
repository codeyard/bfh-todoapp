package model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserManager {
    private static Set<User> users = new HashSet<>();

    public UserManager() {
        loadUsers();
    }

    public User register(String userName, String password) throws UserException {
        if (!isRegistered(userName)) {
            User newUser = new User(userName, password);
            users.add(newUser);
            return newUser;
        } else {
            throw new UserException("Username is not unique!");
        }
    }

    public User authenticate(String userName, String password) throws UserException {
        if (!isRegistered(userName)) {
            throw new UserException("model.User does not exist!");
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

    private boolean isRegistered(String userName) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getUserName().equalsIgnoreCase(userName)) {
                return true;
            }
        }
        return false;
    }

    private void loadUsers() {
        users.add(new User("hans", "ueli"));
        users.add(new User("sepp", "trütsch"));
        users.add(new User("fritz", "müller"));
        users.add(new User("alain", "sutter"));
        users.add(new User("frieda", "friedefreude"));
        users.add(new User("braunhild", "schoggi"));
    }
}
