package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User is already an owner of a desk")
public class UserIsAlreadyDeskOwnerException extends Exception {
    public UserIsAlreadyDeskOwnerException(int deskId, int userId) {
        super("User: " + userId + " is already an owner of a desk: " + deskId);

    }
}
