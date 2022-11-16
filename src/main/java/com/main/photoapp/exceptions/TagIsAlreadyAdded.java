package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Tag is already added to photo")
public class TagIsAlreadyAdded extends Exception {
    public TagIsAlreadyAdded(int tagId, int photoId) {
        super("Photo: " + photoId + " already has tag: " + tagId + " added");
    }
}
