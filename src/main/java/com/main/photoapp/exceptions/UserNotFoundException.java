package com.main.photoapp.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(int id) {
        super("User with id: " + id + " not found");
    }
}
