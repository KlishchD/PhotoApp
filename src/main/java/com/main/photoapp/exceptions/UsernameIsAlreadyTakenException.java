package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Username is already taken")
public class UsernameIsAlreadyTakenException extends Exception {

    public UsernameIsAlreadyTakenException(String username) {
        super("User with username: " + username + " already exists");
    }
}
