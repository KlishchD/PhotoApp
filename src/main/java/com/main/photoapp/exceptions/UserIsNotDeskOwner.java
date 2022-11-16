package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User is not desk owner")
public class UserIsNotDeskOwner extends Exception {
    public UserIsNotDeskOwner(int deskId, int userId) {
        super("User: " + userId + " is not owner of desk: " + deskId);
    }
}
