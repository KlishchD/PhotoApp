package com.main.photoapp.utils;

import org.springframework.web.multipart.MultipartFile;

public class PhotoNameGenerator {

    public static String generatePhotoName(MultipartFile photo) {
        return photo.hashCode() + "_" + photo.getOriginalFilename();
    }

}
