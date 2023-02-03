package com.main.photoapp.services.Desks;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import com.main.photoapp.models.Photo;
import com.main.photoapp.repositories.DesksPhotoMappingRepository;
import com.main.photoapp.services.Photos.PhotoService;
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
    private final UsersService usersService;

    private final DesksOwnerService desksOwnerService;

    private final DesksService desksService;

    private final DesksPhotoMappingRepository photosRepository;

    private final PhotoService photoService;

    @Autowired
    public DesksPhotoService(UsersService usersService, DesksOwnerService desksOwnerService, DesksService desksService, DesksPhotoMappingRepository photosRepository, PhotoService photoService) {
        this.usersService = usersService;
        this.desksOwnerService = desksOwnerService;
        this.desksService = desksService;
        this.photosRepository = photosRepository;
        this.photoService = photoService;
    }

    public boolean isPhotoPartOfDesk(int deskId, int photoId) {
        return photosRepository.existsById(new DeskPhotoMappingId(deskId, photoId));
    }

    public void addPhotoToTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException, PhotoNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (photoService.photoNotExists(photoId)) throw new PhotoNotFoundException(photoId);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canAddPhoto())
            throw new NotEnoughPermissionsException(userId);
        photosRepository.save(new DeskPhotoMapping(deskId, photoId));
    }

    @Transactional
    public void removePhotoFromTable(int deskId, int photoId, int userId) throws DeskNotFoundException, NotEnoughPermissionsException, NoSuchPhotoOnDesk, UserNotFoundException, PhotoNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (photoIsNotPartOfDesk(deskId, photoId)) throw new NoSuchPhotoOnDesk(deskId, photoId);
        if (photoService.photoNotExists(photoId)) throw new PhotoNotFoundException(photoId);
        if (!desksOwnerService.getDeskOwnerPermission(deskId, userId).canRemovePhoto())
            throw new NotEnoughPermissionsException(userId);
        photosRepository.deleteByDeskIdAndPhotoId(deskId, photoId);
    }


    public Page<Photo> getPhotosFromDesk(int deskId, int userId, int page, int pageSize) throws DeskNotFoundException, NotEnoughPermissionsException, UserNotFoundException {
        if (usersService.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (desksService.deskNotExists(deskId)) throw new DeskNotFoundException(deskId);
        if (!desksOwnerService.canUserAccessDesk(deskId, userId)) throw new NotEnoughPermissionsException(userId);
        return photosRepository.findAllByDeskId(deskId, Pageable.ofSize(pageSize).withPage(page))
                .map(DeskPhotoMapping::getPhotoId)
                .map(this::getPhotoById);
    }

    public int getPageNumber(int deskId, int size) {
        int count = photosRepository.countDistinctByDeskId(deskId);
        return  count / size + (count % size == 0 ? 0 : 1);
    }

    public Map<Integer, Photo> getFirstPhotoForDesks(List<Desk> desksIds) {
        return getFirstPhotoForDesksByIds(desksIds.stream().map(Desk::getId).toList());
    }

    public Map<Integer, Photo> getFirstPhotoForDesksByIds(List<Integer> desksIds) {
        Map<Integer, Photo> photos = new HashMap<>();
        for (int deskId: desksIds) {
            photos.put(deskId, getFirstPhotoForDesk(deskId));
        }
        return photos;
    }

    public Photo getFirstPhotoForDesk(int deskId) {
        return photosRepository.findFirstByDeskId(deskId)
                .map(DeskPhotoMapping::getPhotoId)
                .map(this::getPhotoById)
                .orElse(null);
    }

    private Photo getPhotoById(int photoId) {
        try {
            return photoService.getPhotoById(photoId);
        } catch (Exception e){
            return null;
        }
    }

    private boolean photoIsNotPartOfDesk(int deskId, int photoId) {
        return !photosRepository.existsById(new DeskPhotoMappingId(deskId, photoId));
    }
}
