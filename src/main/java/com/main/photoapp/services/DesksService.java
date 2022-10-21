package com.main.photoapp.services;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import com.main.photoapp.repositories.DeskOwnerRepository;
import com.main.photoapp.repositories.DeskPhotoMappingRepository;
import com.main.photoapp.repositories.DeskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DesksService {
    @Autowired
    private DeskRepository desks;

    @Autowired
    private DeskOwnerRepository owners;

    @Autowired
    private DeskPhotoMappingRepository photos;

    @Autowired
    private UsersService usersService;

    @Transactional
    public int addDesk(String name, String description, int creatorId) throws UserNotFoundException {
        if (usersService.userNotExists(creatorId)) throw new UserNotFoundException(creatorId);
        int deskId = desks.save(new Desk(name, description)).getId();
        owners.saveAndFlush(new DeskOwnerMapping(deskId, creatorId, DeskOwnerMapping.Permission.CREATOR_PERMISSION));
        return deskId;
    }

    private DeskOwnerMapping.Permission getDeskOwnerPermission(int deskId, int userId) {
        return owners.findByDeskIdAndUserId(deskId, userId).map(DeskOwnerMapping::getPermission).orElse(DeskOwnerMapping.Permission.NO_PERMISSIONS);
    }

    private boolean isAnOwnerOfDesk(int deskId, int userId) {
        return owners.existsByDeskIdAndUserId(deskId, userId);
    }

    private void addOwnerToDeskValidateUserPermissions(int deskId, int adderId, DeskOwnerMapping.Permission permission) throws NotEnoughPermissionsException, CanNotAddAnotherCreator {
        DeskOwnerMapping.Permission adderPermission = getDeskOwnerPermission(deskId, adderId);
        if (!adderPermission.canAddOwner()) throw new NotEnoughPermissionsException(adderId);
        if (adderPermission == DeskOwnerMapping.Permission.CREATOR_PERMISSION &&
            permission == DeskOwnerMapping.Permission.CREATOR_PERMISSION) throw new CanNotAddAnotherCreator();
        if (permission.getLevel() >= adderPermission.getLevel()) throw new NotEnoughPermissionsException(adderId);
    }

    @Transactional
    public void addOwnerToDesk(int deskId, int userId, DeskOwnerMapping.Permission permission, int adderId) throws NotEnoughPermissionsException, UserIsAlreadyDeskOwnerException, DeskNotFoundException, UserNotFoundException, CanNotAddAnotherCreator {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(adderId)) throw new UserNotFoundException(adderId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (isAnOwnerOfDesk(deskId, userId)) throw new UserIsAlreadyDeskOwnerException(deskId, userId);
        addOwnerToDeskValidateUserPermissions(deskId, adderId, permission);
        owners.saveAndFlush(new DeskOwnerMapping(deskId, userId, permission));
    }

    @Transactional
    public void removeDesk(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!getDeskOwnerPermission(deskId, userId).canDeleteDesk()) throw new NotEnoughPermissionsException(userId);
        desks.deleteById(deskId);
        owners.deleteAllByDeskId(deskId);
    }

    @Transactional
    public Desk getDeskInformation(int deskId, int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!getDeskOwnerPermission(deskId, userId).canAccessDesk()) throw new NotEnoughPermissionsException(userId);
        return desks.findById(deskId).orElse(null);
    }

    @Transactional
    public List<DeskOwnerMapping> getOwners(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!getDeskOwnerPermission(deskId, userId).canAccessDesk()) throw new NotEnoughPermissionsException(userId);
        return owners.findByDeskId(deskId);
    }

    @Transactional
    public DeskOwnerMapping.Permission getOwnerPermission(int deskId, int userId, int userIdWhoAsks) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(userIdWhoAsks)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!getDeskOwnerPermission(deskId, userIdWhoAsks).canAccessDesk()) throw new NotEnoughPermissionsException(userIdWhoAsks);
        return getDeskOwnerPermission(deskId, userId);
    }

    public void addPhotoToTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        //TODO: Add photo existence check
        if (!getDeskOwnerPermission(deskId, userId).canAddPhoto()) throw new NotEnoughPermissionsException(userId);
        photos.save(new DeskPhotoMapping(deskId, photoId));
    }

    public void removePhotoFromTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, NoSuchPhotoOnDesk, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (photoIsNotPartOfDesk(deskId, photoId)) throw new NoSuchPhotoOnDesk(deskId, photoId);
        //TODO: Add photo existence check
        if (!getDeskOwnerPermission(deskId, userId).canRemovePhoto()) throw new NotEnoughPermissionsException(userId);
        photos.deleteByDeskIdAndPhotoId(deskId, photoId);
    }

    public List<Integer> getIdsOfUsersWithSpecificPermissionLevelInDesk(int deskId, int userId, DeskOwnerMapping.Permission permission) throws DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!canUserAccessDesk(deskId, userId)) return null;
        return getIdsOfUsersWithSpecificPermissionLevelInDesk(deskId, permission);
    }

    public Page<Integer> getPhotosFromDesk(int deskId, int userId, int pageSize) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return photos.findAllByDeskId(deskId, Pageable.ofSize(pageSize)).map(DeskPhotoMapping::getPhotoId);
    }


    private boolean canUserAccessDesk(int deskId, int userId) {
        DeskOwnerMapping.Permission permission = owners.findByDeskIdAndUserId(deskId, userId).map(DeskOwnerMapping::getPermission).orElse(DeskOwnerMapping.Permission.NO_PERMISSIONS);
        return permission != DeskOwnerMapping.Permission.NO_PERMISSIONS;
    }
    private boolean photoIsNotPartOfDesk(int deskId, int photoId) {
        return !photos.existsById(new DeskPhotoMappingId(deskId, photoId));
    }

    private boolean deskNotExists(int deskId) {
        return !desks.existsById(deskId);
    }

    private List<Integer> getIdsOfUsersWithSpecificPermissionLevelInDesk(int deskId, DeskOwnerMapping.Permission permission) {
        return owners.findByDeskIdAndPermission(deskId, permission).stream().map(DeskOwnerMapping::getUserId).toList();
    }
}
