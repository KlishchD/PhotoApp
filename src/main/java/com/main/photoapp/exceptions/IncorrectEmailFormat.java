package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Email is in incorrect format")
public class IncorrectEmailFormat extends Exception {
    public IncorrectEmailFormat(String email) {
        super("Email: " + email + " is in incorrect format");
    }
}
