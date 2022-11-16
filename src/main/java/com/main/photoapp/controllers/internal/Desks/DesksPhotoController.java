package com.main.photoapp.controllers.internal.Desks;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NoSuchPhotoOnDesk;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.services.Desks.DesksPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class DesksPhotoController {
    @Autowired
    private DesksPhotoService service;

    @PostMapping("/desk/add/photo")
    public void addPhotoToTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        service.addPhotoToTable(deskId, photoId, userId);
    }

    @GetMapping("/desk/is_photo")
    public boolean isPhotoPartOfDesk(@RequestParam int deskId, @RequestParam int photoId) {
        return service.isPhotoPartOfDesk(deskId, photoId);
    }

    @PostMapping("/desk/remove/photo")
    public void removePhotoFromTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, NoSuchPhotoOnDesk, UserNotFoundException {
        service.removePhotoFromTable(deskId, photoId, userId);
    }
}
