package model;

/**
 * The exception UserException is thrown when registering or authenticating a user has failed.
 *
 * @author Igor Stojanovic, Sabina Löffel, Christophe Leupi, Raphael Gerber
 * @version 1.0
 */
public class UserException extends Exception {

    /**
     * Constructs a User Exception
     *
     * @param message the Message to be thrown
     */
    public UserException(String message) {
        super(message);
    }

}
