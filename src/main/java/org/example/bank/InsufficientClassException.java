package org.example.bank;

public class InsufficientClassException extends RuntimeException {
    public InsufficientClassException(String message) {
        super(message);
    }
}
