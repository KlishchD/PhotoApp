package com.main.photoapp.services.Photos.Storages;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PhotoStorage {
    String uploadPhoto(MultipartFile photo) throws IOException;
}
