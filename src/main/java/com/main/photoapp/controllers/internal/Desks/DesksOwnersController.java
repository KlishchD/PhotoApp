package com.main.photoapp.controllers.internal.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.services.Desks.DesksOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DesksOwnersController {
    @Autowired
    private DesksOwnerService service;

    @PostMapping("/desk/add/owner")
    public void addOwnerToDesk(@RequestParam int deskId, @RequestParam int userId, @RequestParam DeskOwnerMapping.Permission permission, @RequestParam int adderId) throws NotEnoughPermissionsException, UserIsAlreadyDeskOwnerException, DeskNotFoundException, UserNotFoundException, CanNotAddAnotherCreator {
        service.addOwnerToDesk(deskId, userId, permission, adderId);
    }

    @PostMapping("/desk/remove/owner")
    public void removeOwnerFromDesk(@RequestParam int deskId, @RequestParam int userId, @RequestParam int removerId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException, UserIsNotDeskOwner {
        service.removeOwnerFromDesk(deskId, userId, removerId);
    }

//TODO: Think about api url format

    @GetMapping("/desk/get/owners")
    public List<DeskOwnerMapping> getOwners(@RequestParam int deskId, @RequestParam int userId) throws Exception {
        return service.getOwners(deskId, userId);
    }

    @GetMapping("/desk/get/owner_permission")
    public DeskOwnerMapping.Permission getOwnerPermission(@RequestParam int deskId, @RequestParam int userId, @RequestParam int userIdWhoAsks) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        return service.getOwnerPermission(deskId, userId, userIdWhoAsks);
    }

    @GetMapping("/desk/get/creator")
    public Integer getCreator(@RequestParam int deskId, @RequestParam int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        return service.getCreator(deskId, userId);
    }

    @GetMapping("/desk/get/owners_with_permission")
    public List<Integer> getOwnersWithPermission(@RequestParam int deskId, @RequestParam int userId, @RequestParam DeskOwnerMapping.Permission permission) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        return service.getIdsOfUsersWithSpecificPermissionInDesk(deskId, userId, permission);
    }

    @GetMapping("/desk/get/manageable_desks_by_name")
    public List<Desk> getManageableDesksByName(@RequestParam int userId, @RequestParam String name) throws UserNotFoundException {
        return service.getManageableDesksByName(userId, name);
    }
}
