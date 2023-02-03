package com.main.photoapp.services.Photos.Storages;

import com.google.cloud.storage.Bucket;
import com.main.photoapp.utils.GCPUtils;
import com.main.photoapp.utils.PhotoNameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class GCPPhotoStorage implements PhotoStorage {
    private final String bucketURL;

    private final Bucket bucket;

    public GCPPhotoStorage(@Value("${app.photoStorageService.gcp.projectId}") String projectId,
                           @Value("${app.photoStorageService.gcp.bucketName}") String bucketName,
                           @Value("${app.photoStorageService.gcp.credentialsPath}") String credentialsPath) throws IOException {
        bucket = GCPUtils.createBucket(projectId, bucketName, credentialsPath);
        bucketURL = "https://storage.googleapis.com/" + bucketName + "/";
    }

    @Override
    public String uploadPhoto(MultipartFile photo) throws IOException {
        String filename = PhotoNameGenerator.generatePhotoName(photo);
        bucket.create(filename, photo.getBytes());
        return bucketURL + filename;
    }
}

