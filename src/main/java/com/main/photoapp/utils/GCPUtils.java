package com.main.photoapp.utils;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class GCPUtils {
    public static Bucket createBucket(String projectId, String bucketName, String credentialsPath) throws IOException {
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build();
        return options.getService().get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));
    }
}
