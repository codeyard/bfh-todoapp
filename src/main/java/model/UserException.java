package model;

/**
 * The exception UserException is thrown when registering or authenticating a user has failed.
 */
public class UserException extends Exception {

    public UserException(String message) {
        super(message);
    }

}
