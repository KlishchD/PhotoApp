package com.main.photoapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Tag is not found")
public class TagNotFoundException extends Exception {
    public TagNotFoundException(int id) {
        super("Tag with id: " + id + " was not found");
    }
}
