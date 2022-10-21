package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Desk not found")
public class DeskNotFoundException extends Exception {
    public DeskNotFoundException(int deskId) {
        super("There is no desk with id: " + deskId);
    }
}
