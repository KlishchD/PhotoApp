package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Photo not found")
public class PhotoNotFoundException extends Exception {
    public PhotoNotFoundException(int id) {
        super("Photo: " + id + " not found");
    }
}
