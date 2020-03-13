package ch.uzh.ifi.seal.soprafs20.exceptions;

// exception called if a user creation fails because the username/name is already taken
public class DuplicatedUserException extends RuntimeException{
    public DuplicatedUserException(String message){
        super(message);
    }
}
