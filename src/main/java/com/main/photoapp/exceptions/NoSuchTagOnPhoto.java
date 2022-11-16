package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Photo has no such tag attached to it")
public class NoSuchTagOnPhoto extends Exception {
    public NoSuchTagOnPhoto(int tagId, int photoId) {
        super("Photo: " + photoId + " has no tag: " + tagId + " attached to it");
    }
}
