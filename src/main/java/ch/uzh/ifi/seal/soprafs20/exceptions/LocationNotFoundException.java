package ch.uzh.ifi.seal.soprafs20.exceptions;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(String message){
        super(message);
    }
}
