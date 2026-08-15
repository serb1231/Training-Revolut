package org.example.bank;

public class AccountNonExistentException extends RuntimeException {
    public AccountNonExistentException(String message) {
        super(message);
    }
}
