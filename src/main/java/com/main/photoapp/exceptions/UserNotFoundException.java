package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User not found")
public class UserNotFoundException extends Exception {
    public UserNotFoundException(int id) {
        super("User with id: " + id + " not found");
    }
    public UserNotFoundException(String username) {
        super("User with username: " + username + " not found");
    }

}
