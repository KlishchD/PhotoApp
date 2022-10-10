package com.main.photoapp.exceptions;

public class NoSuchDeskFoundException extends Exception {
    public NoSuchDeskFoundException(int deskId) {
        super("There is no desk with id: " + deskId);
    }
}
