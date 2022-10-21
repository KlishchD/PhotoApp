package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Only original creator of a desk can have creator permission")
public class CanNotAddAnotherCreator extends Exception {
    public CanNotAddAnotherCreator() {
        super("Only original creator of a desk can have creator permission");
    }
}
