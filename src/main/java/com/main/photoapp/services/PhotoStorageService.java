package com.main.photoapp.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

@Component
public class PhotoStorageService {
    private final String PROJECT_ID = "photos-366411";
    private final String BUCKET_NAME = "photo_app_bucket_1";

    Credentials credentials =  GoogleCredentials.fromStream(new FileInputStream("src/main/resources/photos-366411-6f9b965d2c39.json"));
    Bucket bucket = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService().get(BUCKET_NAME, Storage.BucketGetOption.fields(Storage.BucketField.values()));


    public PhotoStorageService() throws IOException {
    }

    public String uploadPhoto(MultipartFile photo) throws IOException {
        String filename = photo.hashCode() + "_" + photo.getOriginalFilename();
        Blob blob = bucket.create(filename, photo.getBytes());
        return "https://storage.googleapis.com/" + BUCKET_NAME + "/" + filename;
    }

    public String getName(){
        return bucket.getName();
    }
}

