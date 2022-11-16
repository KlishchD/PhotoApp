package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NoSuchPhotoOnDesk;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import com.main.photoapp.repositories.DesksPhotoMappingRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DesksPhotoService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private DesksOwnerService desksOwnerService;
    @Autowired
    private DesksService desksService;
    @Autowired
    private DesksPhotoMappingRepository photos;

    public boolean isPhotoPartOfDesk(int deskId, int photoId) {
        return photos.existsById(new DeskPhotoMappingId(deskId, photoId));
    }

    public void addPhotoToTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        //TODO: Add photo existence check
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canAddPhoto())
            throw new NotEnoughPermissionsException(userId);
        photos.save(new DeskPhotoMapping(deskId, photoId));
    }

    @Transactional
    public void removePhotoFromTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, NoSuchPhotoOnDesk, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (photoIsNotPartOfDesk(deskId, photoId)) throw new NoSuchPhotoOnDesk(deskId, photoId);
        //TODO: Add photo existence check
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canRemovePhoto())
            throw new NotEnoughPermissionsException(userId);
        photos.deleteByDeskIdAndPhotoId(deskId, photoId);
    }


    public Page<Integer> getPhotosFromDesk(int deskId, int userId, int page, int pageSize) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return photos.findAllByDeskId(deskId, Pageable.ofSize(pageSize).withPage(page)).map(DeskPhotoMapping::getPhotoId);
    }

    public int getPageNumber(int deskId, int size) {
        int count = photos.countDistinctByDeskId(deskId);
        return  count / size + (count % size == 0 ? 0 : 1);
    }

    public Map<Integer, Integer> getFirstPhotoForDesks(List<Desk> desksIds) {
        return getFirstPhotoForDesksByIds(desksIds.stream().map(Desk::getId).toList());
    }

    public Map<Integer, Integer> getFirstPhotoForDesksByIds(List<Integer> desksIds) {
        Map<Integer, Integer> photos = new HashMap<>();
        for (int deskId: desksIds) {
            photos.put(deskId, getFirstPhotoForDesk(deskId));
        }
        return photos;
    }

    public Integer getFirstPhotoForDesk(int deskId) {
        return photos.findFirstByDeskId(deskId).map(DeskPhotoMapping::getPhotoId).orElse(null);
    }

    private boolean photoIsNotPartOfDesk(int deskId, int photoId) {
        return !photos.existsById(new DeskPhotoMappingId(deskId, photoId));
    }
}
