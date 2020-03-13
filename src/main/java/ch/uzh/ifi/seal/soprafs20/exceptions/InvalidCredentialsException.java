package ch.uzh.ifi.seal.soprafs20.exceptions;

// exception called if a user login fails because the credentials are invalid
public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
