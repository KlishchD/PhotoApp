package com.main.photoapp.exceptions;

public class NotEnoughPermissionsException extends Exception {
    public NotEnoughPermissionsException(int userId) {
        super("User: " + userId + " has no permissions to do that");
    }
}
