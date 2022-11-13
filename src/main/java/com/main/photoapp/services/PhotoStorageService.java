package com.main.photoapp.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class PhotoStorageService {

    Credentials credentials =  GoogleCredentials.fromStream(new FileInputStream("src/main/resources/photos-366411-6f9b965d2c39.json"));
    Bucket bucket = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("photos-366411").build().getService().get("photo_app_bucket_1", Storage.BucketGetOption.fields(Storage.BucketField.values()));

    public PhotoStorageService() throws IOException {
    }

    public String getName(){
        return bucket.getName();
    }
}
