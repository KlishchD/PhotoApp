package com.main.photoapp.exceptions;

public class NoSuchPhotoOnDesk extends Exception {
    public NoSuchPhotoOnDesk(int deskId, int photoId) {
        super("There is no photo: " + photoId + " on a desk: " + deskId);
    }
}
