package com.main.photoapp.controllers;

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
    @Autowired
    private PhotoService service;

    @PostMapping("/photo/create")
    @ResponseBody
    public int createPhoto(@RequestParam String description, @RequestParam String paths, @RequestParam int ownerId) {
        return service.createPhoto(description, paths, ownerId);
    }

    @GetMapping("/photo/find/by/id")
    @ResponseBody
    public Photo getPhoto(@RequestParam int id) {
        return service.getPhotoById(id);
    }

    @PostMapping("/photo/remove")
    @ResponseBody
    public void removePhoto(@RequestParam int id) {
        service.removePhoto(id);
    }
}
