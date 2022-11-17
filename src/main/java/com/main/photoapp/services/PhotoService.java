package com.main.photoapp.services;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Photo;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.PhotosRepositoriy;
import com.main.photoapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.main.photoapp.utils.EmailChecker.isEmailCorrect;


@Component
public class PhotoService {
    @Autowired
    private PhotosRepositoriy photos;

    public int createPhoto(String description, String paths, int ownerId) {
        return photos.save(new Photo(description, paths, ownerId)).getId();
    }

    public Photo getPhotoById(int id) {

        return photos.findById(id).get();
    }

    public void removePhoto(int id) {
        photos.deleteById(id);
    }

    public void updatePhotoDescription(int id, String description) {

        Photo photo = photos.findById(id).orElseThrow();
        photo.setDescription(description);
        photos.save(photo);
    }

}
