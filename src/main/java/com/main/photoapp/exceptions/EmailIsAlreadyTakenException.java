package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Email is already taken")
public class EmailIsAlreadyTakenException extends Exception {
    public EmailIsAlreadyTakenException(String email) {
        super("Email: " + email + " is already taken");
    }
}
