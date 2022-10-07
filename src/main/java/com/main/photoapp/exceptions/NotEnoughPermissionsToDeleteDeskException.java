package com.main.photoapp.exceptions;

public class NotEnoughPermissionsToDeleteDeskException extends Exception {
    public NotEnoughPermissionsToDeleteDeskException(int userId, int deskId) {
        super("User with id: " + userId + " is not creator and has no permissions to delete desk with id: " + deskId);
    }
}
