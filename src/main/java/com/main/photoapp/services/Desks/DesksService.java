package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.repositories.DesksRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.main.photoapp.utils.DeskDescriptionChecker.isDeskDescriptionCorrect;
import static com.main.photoapp.utils.DeskNameChecker.isDeskNameCorrect;

@Component
public class DesksService {
    @Autowired
    private DesksRepository desks;

    @Autowired
    private DesksOwnerService desksOwnerService;

    @Autowired
    private UsersService usersService;

    @Transactional
    public int addDesk(String name, String description, int creatorId, Desk.DeskType type) throws UserNotFoundException, IncorrectDeskNameFormat, IncorrectDeskDescriptionFormat {
        if (!isDeskNameCorrect(name)) throw new IncorrectDeskNameFormat(name);
        if (!isDeskDescriptionCorrect(description)) throw new IncorrectDeskDescriptionFormat(description);
        if (usersService.userNotExists(creatorId)) throw new UserNotFoundException(creatorId);
        int deskId = desks.save(new Desk(name, description, type)).getId();
        desksOwnerService.addCreatorForDesk(deskId, creatorId);
        return deskId;
    }

    @Transactional
    public void removeDesk(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canDeleteDesk())
            throw new NotEnoughPermissionsException(userId);
        desks.deleteById(deskId);
        desksOwnerService.removeAllOwnersForDesk(deskId);
    }

    @Transactional
    public Desk getDeskInformation(int deskId, int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return desks.findById(deskId).orElse(null);
    }

    @Transactional
    public void updateDesksName(int deskId, String name, int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskNameFormat, NotEnoughPermissionsException {
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (!isDeskNameCorrect(name)) throw new IncorrectDeskNameFormat(name);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canModifyDeskInformation())
            throw new NotEnoughPermissionsException(userId);
        Desk desk = desks.findById(deskId).get();
        desk.setName(name);
        desks.save(desk);
    }

    @Transactional
    public void updateDesksDescription(int deskId, String description, int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskDescriptionFormat, NotEnoughPermissionsException {
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (!isDeskDescriptionCorrect(description)) throw new IncorrectDeskDescriptionFormat(description);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canModifyDeskInformation())
            throw new NotEnoughPermissionsException(userId);
        Desk desk = desks.findById(deskId).get();
        desk.setDescription(description);
        desks.save(desk);
    }

    @Transactional
    public void updateDesk(int deskId, Desk desk, int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskDescriptionFormat, NotEnoughPermissionsException, IncorrectDeskNameFormat {
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (!isDeskDescriptionCorrect(desk.getDescription()))
            throw new IncorrectDeskDescriptionFormat(desk.getDescription());
        if (!isDeskNameCorrect(desk.getName())) throw new IncorrectDeskNameFormat(desk.getName());
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canModifyDeskInformation())
            throw new NotEnoughPermissionsException(userId);
        desk.setId(deskId);
        desks.save(desk);
    }

    public List<Desk> getAllPublicDesks() {
        return desks.findAllByType(Desk.DeskType.PUBLIC);
    }

    private Set<Desk> getDesksByPermissions(int userId, List<DeskOwnerMapping.Permission> permissions) {
        return permissions.stream()
                .map(permission -> desksOwnerService.getDesksForUserWithPermission(userId, permission))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    private Set<Desk> getDesksByType(List<Desk.DeskType> types) {
        return types.stream()
                .map(desks::findAllByType)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }


    private List<Desk> getAccessibleDesks(int userId, Collection<Desk> desks) {
        return desks.stream()
                .filter(desk -> desksOwnerService.canUserAccessDesk(desk.getId(), userId))
                .collect(Collectors.toList());
    }

    private List<Integer> getAccessibleDesksByIds(int userId, Collection<Integer> desksIds) {
        return desksIds.stream()
                .filter(deskId -> desksOwnerService.canUserAccessDesk(deskId, userId))
                .collect(Collectors.toList());
    }

    public List<Desk> getAllDesksForUser(int userId, List<DeskOwnerMapping.Permission> permissions, List<Desk.DeskType> types) {
        List<Desk> byPermissions = getAccessibleDesks(userId, getDesksByPermissions(userId, permissions));
        List<Desk> byTypes = getAccessibleDesks(userId, getDesksByType(types));
        byPermissions.retainAll(byTypes);
        return byPermissions;
    }

    public List<Desk> getAllDesks() {
        return desks.findAll();
    }

    protected boolean deskNotExists(int deskId) {
        return !desks.existsById(deskId);
    }

    protected boolean isDeskPublic(int deskId) {
        return desks.findById(deskId).get().getType() == Desk.DeskType.PUBLIC;
    }

    protected boolean isDeskPrivate(int deskId) {
        return desks.findById(deskId).get().getType() == Desk.DeskType.PRIVATE;
    }

    protected List<Desk> getDesksById(List<Integer> desksIds) {
        return desksIds.stream().map(this::getDeskById).collect(Collectors.toList());
    }

    protected Desk getDeskById(int deskId) {
        return desks.findById(deskId).get();
    }

}
