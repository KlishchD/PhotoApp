package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Username is in incorrect format")
public class IncorrectUsernameFormat extends Exception {
    public IncorrectUsernameFormat(String username) {
        super("Username : " + username + " is in incorrect format");
    }
}
