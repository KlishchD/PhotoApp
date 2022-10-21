package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Not enough permissions")
public class NotEnoughPermissionsException extends Exception {
    public NotEnoughPermissionsException(int userId) {
        super("User: " + userId + " has no permissions to do that");
    }
}
