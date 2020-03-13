package ch.uzh.ifi.seal.soprafs20.exceptions;

// exception called if a user get/update fails because the user doesn't exist
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
