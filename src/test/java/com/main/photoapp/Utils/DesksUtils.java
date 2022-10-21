package com.main.photoapp.Utils;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.services.DesksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.*;

@Component
public class DesksUtils {

    @Autowired
    private DesksService desksService;

    @Autowired
    private UsersUtils usersUtils;

    public String createDesk(String creatorId, Random random) throws UserNotFoundException {
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), Integer.parseInt(creatorId));
        return String.valueOf(id);
    }

    public String createDesk(Random random) throws Exception {
        int creatorId = Integer.parseInt(usersUtils.createUser(random));
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), creatorId);
        return String.valueOf(id);
    }

    public void addOwner(String deskId, String userId, DeskOwnerMapping.Permission permission, String adderId) throws DeskNotFoundException, UserNotFoundException, CanNotAddAnotherCreator, UserIsAlreadyDeskOwnerException, NotEnoughPermissionsException {
        desksService.addOwnerToDesk(Integer.parseInt(deskId), Integer.parseInt(userId), permission, Integer.parseInt(adderId));
    }

    public String createDesk(String name, String description, String creatorId) throws UserNotFoundException {
        return String.valueOf(desksService.addDesk(name, description, Integer.parseInt(creatorId)));
    }
}
