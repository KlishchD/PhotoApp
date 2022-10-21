package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Desk name is in incorrect format")
public class IncorrectDeskNameFormat extends Exception {
    public IncorrectDeskNameFormat(String name) {
        super("Desk name: " + name + " is in incorrect format");
    }
}
