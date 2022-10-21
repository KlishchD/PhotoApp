package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import com.main.photoapp.repositories.DeskOwnerRepository;
import com.main.photoapp.repositories.DeskPhotoMappingRepository;
import com.main.photoapp.repositories.DeskRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.main.photoapp.utils.DeskDescriptionChecker.isDeskDescriptionCorrect;
import static com.main.photoapp.utils.DeskNameChecker.isDeskNameCorrect;

@Component
public class DesksService {
    @Autowired
    private DeskRepository desks;

    @Autowired
    private DesksOwnerService desksOwnerService;

    @Autowired
    private UsersService usersService;

    @Transactional
    public int addDesk(String name, String description, int creatorId) throws UserNotFoundException, IncorrectDeskNameFormat, IncorrectDeskDescriptionFormat {
        if (!isDeskNameCorrect(name)) throw new IncorrectDeskNameFormat(name);
        if (!isDeskDescriptionCorrect(description)) throw new IncorrectDeskDescriptionFormat(description);
        if (usersService.userNotExists(creatorId)) throw new UserNotFoundException(creatorId);
        int deskId = desks.save(new Desk(name, description)).getId();
        desksOwnerService.addCreatorForDesk(deskId, creatorId);
        return deskId;
    }

    @Transactional
    public void removeDesk(int deskId, int userId) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canDeleteDesk()) throw new NotEnoughPermissionsException(userId);
        desks.deleteById(deskId);
        desksOwnerService.removeAllOwnersForDesk(deskId);
    }

    @Transactional
    public Desk getDeskInformation(int deskId, int userId) throws NotEnoughPermissionsException, DeskNotFoundException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canAccessDesk()) throw new NotEnoughPermissionsException(userId);
        return desks.findById(deskId).orElse(null);
    }

    public void updateDesksName(int deskId, String name, int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskNameFormat {
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (!isDeskNameCorrect(name)) throw new IncorrectDeskNameFormat(name);
        Desk desk = desks.findById(deskId).get();
        desk.setName(name);
        desks.save(desk);
    }
    public void updateDesksDescription(int deskId, String description, int userId) throws DeskNotFoundException, UserNotFoundException, IncorrectDeskDescriptionFormat {
        if (deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (!isDeskDescriptionCorrect(description)) throw new IncorrectDeskDescriptionFormat(description);
        Desk desk = desks.findById(deskId).get();
        desk.setDescription(description);
        desks.save(desk);
    }

    protected boolean deskNotExists(int deskId) {
        return !desks.existsById(deskId);
    }
}
