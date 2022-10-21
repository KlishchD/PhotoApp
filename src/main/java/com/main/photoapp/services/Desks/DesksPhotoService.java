package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NoSuchPhotoOnDesk;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import com.main.photoapp.repositories.DeskPhotoMappingRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class DesksPhotoService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private DesksOwnerService desksOwnerService;
    @Autowired
    private DesksService desksService;
    @Autowired
    private DeskPhotoMappingRepository photos;

    public void addPhotoToTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        //TODO: Add photo existence check
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canAddPhoto())
            throw new NotEnoughPermissionsException(userId);
        photos.save(new DeskPhotoMapping(deskId, photoId));
    }

    public void removePhotoFromTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, NoSuchPhotoOnDesk, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (photoIsNotPartOfDesk(deskId, photoId)) throw new NoSuchPhotoOnDesk(deskId, photoId);
        //TODO: Add photo existence check
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canRemovePhoto())
            throw new NotEnoughPermissionsException(userId);
        photos.deleteByDeskIdAndPhotoId(deskId, photoId);
    }


    public Page<Integer> getPhotosFromDesk(int deskId, int userId, int pageSize) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return photos.findAllByDeskId(deskId, Pageable.ofSize(pageSize)).map(DeskPhotoMapping::getPhotoId);
    }

    private boolean photoIsNotPartOfDesk(int deskId, int photoId) {
        return !photos.existsById(new DeskPhotoMappingId(deskId, photoId));
    }
}
