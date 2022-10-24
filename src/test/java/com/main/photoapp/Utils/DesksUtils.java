package com.main.photoapp.Utils;

import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.*;

@Component
public class DesksUtils {

    @Autowired
    private DesksService desksService;

    @Autowired
    private DesksOwnerService desksOwnerService;

    @Autowired
    private UsersUtils usersUtils;

    public String createPrivateDesk(String creatorId, Random random) throws Exception {
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), Integer.parseInt(creatorId), Desk.DeskType.PRIVATE);
        return String.valueOf(id);
    }

    public String createPrivateDesk(Random random) throws Exception {
        int creatorId = Integer.parseInt(usersUtils.createUser(random));
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), creatorId, Desk.DeskType.PRIVATE);
        return String.valueOf(id);
    }

    public String createPublicDesk(String creatorId, Random random) throws Exception {
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), Integer.parseInt(creatorId), Desk.DeskType.PUBLIC);
        return String.valueOf(id);
    }

    public String createPublicDesk(Random random) throws Exception {
        int creatorId = Integer.parseInt(usersUtils.createUser(random));
        int id = desksService.addDesk(getRandomName(random), getRandomDescription(random), creatorId, Desk.DeskType.PUBLIC);
        return String.valueOf(id);
    }

    public void addOwner(String deskId, String userId, DeskOwnerMapping.Permission permission, String adderId) throws Exception {
        desksOwnerService.addOwnerToDesk(Integer.parseInt(deskId), Integer.parseInt(userId), permission, Integer.parseInt(adderId));
    }

    public String createPublicDesk(String name, String description, String creatorId) throws Exception {
        return String.valueOf(desksService.addDesk(name, description, Integer.parseInt(creatorId), Desk.DeskType.PUBLIC));
    }

    public String createPrivateDesk(String name, String description, String creatorId) throws Exception {
        return String.valueOf(desksService.addDesk(name, description, Integer.parseInt(creatorId), Desk.DeskType.PRIVATE));
    }
}
