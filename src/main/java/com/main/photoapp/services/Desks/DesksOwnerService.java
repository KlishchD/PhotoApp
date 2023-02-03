package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMappingId;
import com.main.photoapp.repositories.DesksOwnerRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DesksOwnerService {
    private final DesksOwnerRepository owners;

    private final UsersService usersService;

    private DesksService desksService;

    @Autowired
    public DesksOwnerService(DesksOwnerRepository owners, UsersService usersService) {
        this.owners = owners;
        this.usersService = usersService;
    }

    public DesksOwnerService(DesksOwnerRepository owners, UsersService usersService, DesksService desksService) {
        this.owners = owners;
        this.usersService = usersService;
        this.desksService = desksService;
    }

    @Transactional
    public void addOwnerToDesk(int deskId, int userId, DeskOwnerMapping.Permission permission, int adderId) throws NotEnoughPermissionsException, UserIsAlreadyDeskOwnerException, DeskNotFoundException, UserNotFoundException, CanNotAddAnotherCreator {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(adderId)) throw new UserNotFoundException(adderId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (isAnOwnerOfDesk(deskId, userId)) throw new UserIsAlreadyDeskOwnerException(deskId, userId);
        addOwnerToDeskValidateUserPermissions(deskId, adderId, permission);
        owners.save(new DeskOwnerMapping(deskId, userId, permission));
    }

    @Transactional
    public void removeOwnerFromDesk(int deskId, int userId, int removerId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException, UserIsNotDeskOwner {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (usersService.userNotExists(removerId)) throw new UserNotFoundException(removerId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!isAnOwnerOfDesk(deskId, userId)) throw new UserIsNotDeskOwner(deskId, userId);
        removeOwnerFromDeskValidateUserPermissions(deskId, userId, removerId);
        owners.deleteById(new DeskOwnerMappingId(deskId, userId));
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

    public List<Integer> getDesksIdWhereUserIsCreator(int userId) {
        return getDesksForUserWithPermission(userId, DeskOwnerMapping.Permission.CREATOR_PERMISSION)
                .stream()
                .map(Desk::getId)
                .collect(Collectors.toList());
    }

    private List<Desk> getDesksForUserWithNoPermission(int userId) {
        List<Integer> owner = owners.findAllByUserId(userId).stream().map(DeskOwnerMapping::getDeskId).toList();
        return desksService.getAllDesks()
                .stream()
                .filter(desk -> !owner.contains(desk.getId()))
                .toList();
    }

    private List<Desk> getDesksForUserWithPermissionExceptNoPermission(int userId, DeskOwnerMapping.Permission permission) {
        List<Integer> desksIds = owners.findAllByUserIdAndPermission(userId, permission).stream().map(DeskOwnerMapping::getDeskId).toList();
        return desksService.getDesksById(desksIds);
    }

    public List<Desk> getDesksForUserWithPermissions(int userId, List<DeskOwnerMapping.Permission> permissions) {
        return permissions.stream()
                .map(permission -> getDesksForUserWithPermission(userId, permission))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Desk> getDesksForUserWithPermission(int userId, DeskOwnerMapping.Permission permission) {
        return permission == DeskOwnerMapping.Permission.NO_PERMISSIONS ? getDesksForUserWithNoPermission(userId) :
                getDesksForUserWithPermissionExceptNoPermission(userId, permission);
    }

    public List<Integer> getOwnersIds(int deskId) {
        return owners.findByDeskId(deskId).stream().map(DeskOwnerMapping::getUserId).toList();
    }

    public List<String> getOwnersNames(int deskId) {
        List<Integer> ownersIds = getOwnersIds(deskId);
        return usersService.getUsernames(ownersIds);
    }

    public Map<Integer, List<String>> getOwnersNames(List<Desk> desks) throws UserNotFoundException {
        Map<Integer, List<String>> desksOwners = new HashMap<>();
        for (Desk desk : desks) {
            desksOwners.put(desk.getId(), getOwnersNames(desk.getId()));
        }
        return desksOwners;
    }

    public List<Desk> getManageableDesksByName(int userId, String name) throws UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        List<DeskOwnerMapping.Permission> managerPermission = DeskOwnerMapping.Permission.permissionsCanAddPhoto();
        List<Desk> desks = getDesksForUserWithPermissions(userId, managerPermission);
        return desks.stream()
                .filter(desk -> desk.getName().contains(name))
                .collect(Collectors.toList());
    }

    protected void addCreatorForDesk(int deskId, int creatorId) {
        owners.saveAndFlush(new DeskOwnerMapping(deskId, creatorId, DeskOwnerMapping.Permission.CREATOR_PERMISSION));
    }

    protected void removeAllOwnersForDesk(int deskId) {
        owners.deleteAllByDeskId(deskId);
    }

    public DeskOwnerMapping.Permission getDeskOwnerPermission(int deskId, int userId) {
        return owners.findByDeskIdAndUserId(deskId, userId).map(DeskOwnerMapping::getPermission).orElse(DeskOwnerMapping.Permission.NO_PERMISSIONS);
    }

    protected boolean isAnOwnerOfDesk(int deskId, int userId) {
        return owners.existsByDeskIdAndUserId(deskId, userId);
    }

    protected List<Integer> getIdsOfUsersWithSpecificPermissionInDesk(int deskId, DeskOwnerMapping.Permission permission) {
        return owners.findByDeskIdAndPermission(deskId, permission).stream().map(DeskOwnerMapping::getUserId).toList();
    }

    public boolean canUserAccessDesk(int deskId, int userId) {
        return (desksService.isDeskPublic(deskId) && getDeskOwnerPermission(deskId, userId).canAccessPublicDesk()) ||
                (desksService.isDeskPrivate(deskId) && getDeskOwnerPermission(deskId, userId).canAccessPrivateDesk());
    }

    private void addOwnerToDeskValidateUserPermissions(int deskId, int adderId, DeskOwnerMapping.Permission permission) throws NotEnoughPermissionsException, CanNotAddAnotherCreator {
        DeskOwnerMapping.Permission adderPermission = getDeskOwnerPermission(deskId, adderId);
        if (!adderPermission.canAddOwner()) throw new NotEnoughPermissionsException(adderId);
        if (adderPermission == DeskOwnerMapping.Permission.CREATOR_PERMISSION && permission == DeskOwnerMapping.Permission.CREATOR_PERMISSION)
            throw new CanNotAddAnotherCreator();
        if (permission.getLevel() >= adderPermission.getLevel()) throw new NotEnoughPermissionsException(adderId);
    }

    private void removeOwnerFromDeskValidateUserPermissions(int deskId, int userId, int removerId) throws NotEnoughPermissionsException {
        DeskOwnerMapping.Permission removerPermission = getDeskOwnerPermission(deskId, removerId);
        DeskOwnerMapping.Permission userPermission = getDeskOwnerPermission(deskId, userId);
        if (!removerPermission.canRemoveOwner()) throw new NotEnoughPermissionsException(removerId);
        if (removerPermission.getLevel() <= userPermission.getLevel())
            throw new NotEnoughPermissionsException(removerId);
    }

    @Autowired
    public void setDesksService(DesksService desksService) {
        this.desksService = desksService;
    }
}
