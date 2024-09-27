package gr.codehub.rest.webtechnikon.exceptions;

public class MissingInputException extends RuntimeException {
    public MissingInputException(String message) {
        super(message);
    }
}
