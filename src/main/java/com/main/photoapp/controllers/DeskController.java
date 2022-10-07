package com.main.photoapp.controllers;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.services.DeskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DeskController {
    @Autowired
    private DeskService service;

    @PostMapping("/desk/add")
    @ResponseBody
    public int addDesk(@RequestParam String name, @RequestParam String description, @RequestParam("creator_id") int creatorId) {
        return service.addDesk(name, description, creatorId);
    }

    @PostMapping("/desk/add/owner")
    @ResponseBody
    public void addOwnerToDesk(@RequestParam int deskId,
                               @RequestParam int userId,
                               @RequestParam DeskOwnerMapping.Permission permission,
                               @RequestParam int adderId) throws NotEnoughPermissionsException, UserIsAlreadyDeskOwnerException, NoSuchDeskFoundException {
        service.addOwnerToDesk(deskId, userId, permission, adderId);
    }


    @PostMapping("/desk/remove")
    @ResponseBody
    public void removeDesk(@RequestParam int deskId, @RequestParam int userId) throws NotEnoughPermissionsToDeleteDeskException, NoSuchDeskFoundException {
        service.removeDesk(deskId, userId);
    }


    @GetMapping("/desk/get/info")
    @ResponseBody
    public Desk getDeskInformation(@RequestParam int deskId, @RequestParam int userId) throws NotEnoughPermissionsException, NoSuchDeskFoundException {
        return service.getDeskInformation(deskId, userId);
    }

    @GetMapping("/desk/get/owners")
    @ResponseBody
    public List<DeskOwnerMapping> getOwners(@RequestParam int deskId, @RequestParam int userId) throws Exception {
        return service.getOwners(deskId, userId);
    }

    @GetMapping("/desk/get/owner_permission")
    @ResponseBody
    public DeskOwnerMapping.Permission getOwnerPermission(@RequestParam int deskId, @RequestParam int userId, @RequestParam int userIdWhoAsks) throws NotEnoughPermissionsException, NoSuchDeskFoundException {
        return service.getOwnerPermission(deskId, userId, userIdWhoAsks);
    }

    @GetMapping("/desk/get/creator")
    @ResponseBody
    public Integer getCreator(@RequestParam int deskId, @RequestParam int userId) throws NoSuchDeskFoundException {
        return service.getIdsOfUsersWithSpecificPermissionLevelInDesk(deskId, userId, DeskOwnerMapping.Permission.CREATOR_PERMISSION).get(0);
    }

    @GetMapping("/desk/get/owners_with_permission")
    public List<Integer> getOwnersWithPermission(@RequestParam int deskId, @RequestParam int userId, @RequestParam DeskOwnerMapping.Permission permission) throws NoSuchDeskFoundException {
        return service.getIdsOfUsersWithSpecificPermissionLevelInDesk(deskId, userId, permission);
    }

    @PostMapping("/desk/add/photo")
    @ResponseBody
    public void addPhotoToTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, NoSuchDeskFoundException {
        service.addPhotoToTable(deskId, photoId, userId);
    }

    @PostMapping("/desk/remove/photo")
    @ResponseBody
    public void removePhotoFromTable(@RequestParam int deskId, @RequestParam int photoId, @RequestParam int userId) throws NotEnoughPermissionsException, NoSuchDeskFoundException, NoSuchPhotoOnDesk {
        service.removePhotoFromTable(deskId, photoId, userId);
    }

}
