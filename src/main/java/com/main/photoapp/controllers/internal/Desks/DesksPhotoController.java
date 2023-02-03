package com.main.photoapp.controllers.internal.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.services.Desks.DesksPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DesksPhotoController {
    private DesksPhotoService service;

    @Autowired
    public DesksPhotoController(DesksPhotoService service) {
        this.service = service;
    }

    @PostMapping("/desk/add/photo")
    public void addPhotoToTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException, PhotoNotFoundException {
        service.addPhotoToTable(deskId, photoId, userId);
    }

    @GetMapping("/desk/is_photo")
    public boolean isPhotoPartOfDesk(@RequestParam int deskId, @RequestParam int photoId) {
        return service.isPhotoPartOfDesk(deskId, photoId);
    }

    @PostMapping("/desk/remove/photo")
    public void removePhotoFromTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, NoSuchPhotoOnDesk, UserNotFoundException, PhotoNotFoundException {
        service.removePhotoFromTable(deskId, photoId, userId);
    }
}
