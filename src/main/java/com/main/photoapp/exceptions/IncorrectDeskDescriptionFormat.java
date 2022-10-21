package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Desk description is in incorrect format")
public class IncorrectDeskDescriptionFormat extends Exception {
    public IncorrectDeskDescriptionFormat(String description) {
        super("Desk description: " + description + " is in incorrect format");

    }
}
