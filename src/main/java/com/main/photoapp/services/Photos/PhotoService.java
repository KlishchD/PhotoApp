package com.main.photoapp.services.Photos;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Photo;
import com.main.photoapp.repositories.PhotosRepository;
import com.main.photoapp.services.Photos.Storages.PhotoStorage;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.main.photoapp.utils.PhotoDescriptionChecker.isPhotoDescriptionCorrect;


@Component
public class PhotoService {
    private final PhotosRepository photos;

    private final UsersService usersService;

    private final PhotoStorage photoStorage;

    @Autowired
    public PhotoService(PhotosRepository photos, UsersService usersService, PhotoStorage photoStorage) {
        this.photos = photos;
        this.usersService = usersService;
        this.photoStorage = photoStorage;
    }

    public int createPhoto(MultipartFile photo, String description, int ownerId) throws IncorrectPhotoDescriptionFormat, UserNotFoundException, IOException {
        String path = uploadPhoto(photo);
        return createPhoto(description, path, ownerId);
    }

    public int createPhoto(String description, String paths, int ownerId) throws IncorrectPhotoDescriptionFormat, UserNotFoundException {
        if (!isPhotoDescriptionCorrect(description)) throw new IncorrectPhotoDescriptionFormat(description);
        if (usersService.userNotExists(ownerId)) throw new UserNotFoundException(ownerId);
        return photos.save(new Photo(description, paths, ownerId)).getId();
    }

    public Photo getPhotoById(int id) throws PhotoNotFoundException {
        if (photoNotExists(id)) throw new PhotoNotFoundException(id);
        return photos.findById(id).get();
    }

    public void removePhoto(int photoId, int removerId) throws PhotoNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (photoNotExists(photoId)) throw new PhotoNotFoundException(photoId);
        if (usersService.userNotExists(removerId)) throw new UserNotFoundException(removerId);
        if (!isUserPhotoOwner(photoId, removerId)) throw new NotEnoughPermissionsException(removerId);
        photos.deleteById(photoId);
    }

    public Page<Photo> getPhotos(int page, int pageSize) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        return photos.findAll(Pageable.ofSize(pageSize).withPage(page));
    }

    public void updatePhotoDescription(int photoId, String description, int updaterId) throws PhotoNotFoundException, UserNotFoundException, NotEnoughPermissionsException, IncorrectPhotoDescriptionFormat {
        if (photoNotExists(photoId)) throw new PhotoNotFoundException(photoId);
        if (usersService.userNotExists(updaterId)) throw new UserNotFoundException(updaterId);
        if (!isUserPhotoOwner(photoId, updaterId)) throw new NotEnoughPermissionsException(updaterId);
        if (!isPhotoDescriptionCorrect(description)) throw new IncorrectPhotoDescriptionFormat(description);
        Photo photo = photos.findById(photoId).orElseThrow();
        photo.setDescription(description);
        photos.save(photo);
    }

    public boolean isUserPhotoOwner(int photoId, int userId) {
        Photo photo = photos.findById(photoId).get();
        return photo.getOwnerId() == userId;
    }

    public long getPageNumber(int pageSize) {
        long count = photos.count();
        return count / pageSize + (count % pageSize != 0 ? 1 : 0);
    }

    public boolean photoNotExists(int id) {
        return !photos.existsById(id);
    }

    public String uploadPhoto(MultipartFile photo) throws IOException {
        return photoStorage.uploadPhoto(photo);
    }
}
