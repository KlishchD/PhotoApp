package com.main.photoapp.controllers.internal;

import com.main.photoapp.exceptions.IncorrectPhotoDescriptionFormat;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.PhotoNotFoundException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Photo;
import com.main.photoapp.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PhotosController {
    private final PhotoService service;

    @Autowired
    public PhotosController(PhotoService service) {
        this.service = service;
    }

    @PostMapping("/photo/create")
    @ResponseBody
    public int createPhoto(@RequestParam String description, @RequestParam String path, @RequestParam int creatorId) throws UserNotFoundException, IncorrectPhotoDescriptionFormat {
        return service.createPhoto(description, path, creatorId);
    }

    @GetMapping("/photo/find/by/id")
    @ResponseBody
    public Photo getPhoto(@RequestParam int photoId) throws PhotoNotFoundException {
        return service.getPhotoById(photoId);
    }

    @PostMapping("/photo/remove")
    @ResponseBody
    public void removePhoto(@RequestParam int id, @RequestParam int removerId) throws UserNotFoundException, PhotoNotFoundException, NotEnoughPermissionsException {
        service.removePhoto(id, removerId);
    }
}
