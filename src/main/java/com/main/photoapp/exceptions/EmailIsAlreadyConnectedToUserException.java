package com.main.photoapp.exceptions;

public class EmailIsAlreadyConnectedToUserException extends Exception {
    public EmailIsAlreadyConnectedToUserException(String email) {
        super("Email: " + email + " is already connected to another user");
    }
}
