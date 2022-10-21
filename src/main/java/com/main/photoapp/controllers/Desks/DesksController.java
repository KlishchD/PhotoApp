package com.main.photoapp.controllers.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.services.Desks.DesksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DesksController {
    @Autowired
    private DesksService service;

    @PostMapping("/desk/add")
    @ResponseBody
    public int addDesk(@RequestParam String name, @RequestParam String description, @RequestParam("creator_id") int creatorId) throws UserNotFoundException, IncorrectDeskNameFormat, IncorrectDeskDescriptionFormat {
        return service.addDesk(name, description, creatorId);
    }

    @PostMapping("/desk/remove")
    @ResponseBody
    public void removeDesk(@RequestParam int deskId, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        service.removeDesk(deskId, userId);
    }

    @GetMapping("/desk/get/info")
    @ResponseBody
    public Desk getDeskInformation(@RequestParam int deskId, @RequestParam int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        return service.getDeskInformation(deskId, userId);
    }

    @PostMapping("/desk/update/name")
    @ResponseBody
    public void updateDeskName(@RequestParam int deskId, @RequestParam String name, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskNameFormat {
        service.updateDesksName(deskId, name, userId);
    }

    @PostMapping("/desk/update/description")
    @ResponseBody
    public void updateDeskDescription(@RequestParam int deskId, @RequestParam String description, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskDescriptionFormat {
        service.updateDesksDescription(deskId, description, userId);
    }
}
