package com.main.photoapp.controllers.internal.Desks;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NoSuchPhotoOnDesk;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.services.Desks.DesksPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DesksPhotoController {
    @Autowired
    private DesksPhotoService service;

    @PostMapping("/desk/add/photo")
    @ResponseBody
    public void addPhotoToTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        service.addPhotoToTable(deskId, photoId, userId);
    }

    @PostMapping("/desk/remove/photo")
    @ResponseBody
    public void removePhotoFromTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, NoSuchPhotoOnDesk, UserNotFoundException {
        service.removePhotoFromTable(deskId, photoId, userId);
    }

}
