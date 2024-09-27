package gr.codehub.rest.webtechnikon.exceptions;

public class DuplicateEntryException extends RuntimeException{
    public DuplicateEntryException(String message) {
        super(message);
    }
}
