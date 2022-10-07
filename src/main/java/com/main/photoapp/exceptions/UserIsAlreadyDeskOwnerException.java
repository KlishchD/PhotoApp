package com.main.photoapp.exceptions;

public class UserIsAlreadyDeskOwnerException extends Exception {
    public UserIsAlreadyDeskOwnerException(int deskId, int userId) {
        super("User: " + userId + " is already an owner of a desk: " + deskId);

    }
}
