package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Photo description is in incorrect format")
public class IncorrectPhotoDescriptionFormat extends Exception {
    public IncorrectPhotoDescriptionFormat(String description) {
        super("Photo description: " + description + " is in incorrect format");

    }
}
