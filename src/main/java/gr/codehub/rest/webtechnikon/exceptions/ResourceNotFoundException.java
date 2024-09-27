package gr.codehub.rest.webtechnikon.exceptions;


public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
