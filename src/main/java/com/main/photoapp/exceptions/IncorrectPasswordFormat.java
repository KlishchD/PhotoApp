package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Password is in incorrect format")
public class IncorrectPasswordFormat extends Exception {
    public IncorrectPasswordFormat(String password) {
        super("Password : " + password + " is incorrect format");
    }
}
