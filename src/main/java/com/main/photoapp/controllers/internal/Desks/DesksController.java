package com.main.photoapp.controllers.internal.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.services.Desks.DesksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class DesksController {
    @Autowired
    private DesksService service;

    @PostMapping("/desk/add")
    public int addDesk(@RequestParam String name, @RequestParam String description, @RequestParam("creator_id") int creatorId, @RequestParam Desk.DeskType type) throws UserNotFoundException, IncorrectDeskNameFormat, IncorrectDeskDescriptionFormat {
        return service.addDesk(name, description, creatorId, type);
    }

    @PostMapping("/desk/remove")
    public void removeDesk(@RequestParam int deskId, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        service.removeDesk(deskId, userId);
    }

    @GetMapping("/desk/get/info")
    public Desk getDeskInformation(@RequestParam int deskId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        return service.getDeskInformation(deskId, userId);
    }

    @PostMapping("/desk/update/name")
    public void updateDeskName(@RequestParam int deskId, @RequestParam String name, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskNameFormat, NotEnoughPermissionsException {
        service.updateDesksName(deskId, name, userId);
    }

    @PostMapping("/desk/update/description")
    public void updateDeskDescription(@RequestParam int deskId, @RequestParam String description, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskDescriptionFormat, NotEnoughPermissionsException {
        service.updateDesksDescription(deskId, description, userId);
    }
}
