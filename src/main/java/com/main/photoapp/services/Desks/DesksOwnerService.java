package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.repositories.DeskOwnerRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DesksOwnerService {
    @Autowired
    private DeskOwnerRepository owners;

    @Autowired
    private UsersService usersService;

    @Autowired
    private DesksService desksService;

    @Transactional
    public void addOwnerToDesk(int deskId, int userId, DeskOwnerMapping.Permission permission, int adderId) throws NotEnoughPermissionsException, UserIsAlreadyDeskOwnerException, DeskNotFoundException, UserNotFoundException, CanNotAddAnotherCreator {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(adderId)) throw new UserNotFoundException(adderId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (isAnOwnerOfDesk(deskId, userId)) throw new UserIsAlreadyDeskOwnerException(deskId, userId);
        addOwnerToDeskValidateUserPermissions(deskId, adderId, permission);
        owners.saveAndFlush(new DeskOwnerMapping(deskId, userId, permission));
    }

    @Transactional
    public List<DeskOwnerMapping> getOwners(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return owners.findByDeskId(deskId);
    }

    @Transactional
    public DeskOwnerMapping.Permission getOwnerPermission(int deskId, int userId, int userIdWhoAsks) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(userIdWhoAsks)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!canUserAccessDesk(deskId, userIdWhoAsks)) throw new NotEnoughPermissionsException(userIdWhoAsks);
        return getDeskOwnerPermission(deskId, userId);
    }

    public Integer getCreator(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        return getIdsOfUsersWithSpecificPermissionInDesk(deskId, userId, DeskOwnerMapping.Permission.CREATOR_PERMISSION).get(0);
    }

    public List<Integer> getIdsOfUsersWithSpecificPermissionInDesk(int deskId, int userId, DeskOwnerMapping.Permission permission) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return getIdsOfUsersWithSpecificPermissionInDesk(deskId, permission);
    }

    protected void addCreatorForDesk(int deskId, int creatorId) {
        owners.saveAndFlush(new DeskOwnerMapping(deskId, creatorId, DeskOwnerMapping.Permission.CREATOR_PERMISSION));
    }

    protected void removeAllOwnersForDesk(int deskId) {
        owners.deleteAllByDeskId(deskId);
    }

    protected DeskOwnerMapping.Permission getDeskOwnerPermission(int deskId, int userId) {
        return owners.findByDeskIdAndUserId(deskId, userId).map(DeskOwnerMapping::getPermission).orElse(DeskOwnerMapping.Permission.NO_PERMISSIONS);
    }

    protected boolean isAnOwnerOfDesk(int deskId, int userId) {
        return owners.existsByDeskIdAndUserId(deskId, userId);
    }

    protected List<Integer> getIdsOfUsersWithSpecificPermissionInDesk(int deskId, DeskOwnerMapping.Permission permission) {
        return owners.findByDeskIdAndPermission(deskId, permission).stream().map(DeskOwnerMapping::getUserId).toList();
    }

    protected boolean canUserAccessDesk(int deskId, int userId) {
        return (desksService.isDeskPublic(deskId) && getDeskOwnerPermission(deskId, userId).canAccessPublicDesk()) ||
                (desksService.isDeskPrivate(deskId) && getDeskOwnerPermission(deskId, userId).canAccessPrivateDesk());
    }

    private void addOwnerToDeskValidateUserPermissions(int deskId, int adderId, DeskOwnerMapping.Permission permission) throws NotEnoughPermissionsException, CanNotAddAnotherCreator {
        DeskOwnerMapping.Permission adderPermission = getDeskOwnerPermission(deskId, adderId);
        if (!adderPermission.canAddOwner()) throw new NotEnoughPermissionsException(adderId);
        if (adderPermission == DeskOwnerMapping.Permission.CREATOR_PERMISSION && permission == DeskOwnerMapping.Permission.CREATOR_PERMISSION) throw new CanNotAddAnotherCreator();
        if (permission.getLevel() >= adderPermission.getLevel()) throw new NotEnoughPermissionsException(adderId);
    }
}
