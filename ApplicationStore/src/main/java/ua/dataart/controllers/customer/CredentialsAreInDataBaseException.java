package ua.dataart.controllers.customer;

public class CredentialsAreInDataBaseException extends RuntimeException {
    public CredentialsAreInDataBaseException(String message) {
        super(message);
    }
}
