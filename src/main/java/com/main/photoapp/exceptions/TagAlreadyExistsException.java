package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Tag with specified text already exists")
public class TagAlreadyExistsException extends Exception {
    public TagAlreadyExistsException(String text) {
        super("Tag with text: " + text + " already exists");
    }
}
