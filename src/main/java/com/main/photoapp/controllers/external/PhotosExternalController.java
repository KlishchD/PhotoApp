package com.main.photoapp.controllers.external;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Photo;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksPhotoService;
import com.main.photoapp.services.PhotoService;
import com.main.photoapp.services.PhotoStorageService;
import com.main.photoapp.services.Tags.TagsPhotoService;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@ControllerAdvice
@Controller
public class PhotosExternalController extends ExternalControllerBase {
    private final DesksPhotoService desksPhotoService;

    private final DesksOwnerService desksOwnerService;

    private final TagsPhotoService tagsPhotoService;

    private final PhotoStorageService photoStorageService;

    private final UsersService usersService;

    private final PhotoService photoService;

    @Autowired
    public PhotosExternalController(DesksPhotoService desksPhotoService, DesksOwnerService desksOwnerService, TagsPhotoService tagsPhotoService, PhotoStorageService photoStorageService, UsersService usersService, PhotoService photoService) {
        super(usersService);
        this.desksPhotoService = desksPhotoService;
        this.desksOwnerService = desksOwnerService;
        this.tagsPhotoService = tagsPhotoService;
        this.photoStorageService = photoStorageService;
        this.usersService = usersService;
        this.photoService = photoService;
    }

    @GetMapping("/view_photos")
    public String viewPhotos(@RequestParam int deskId, @RequestParam int page, @RequestParam(defaultValue = "2") int pageSize, Model model) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (!desksOwnerService.canUserAccessDesk(deskId, getCurrentUser().getId())) {
            model.addAttribute("error", "Not enough permissions");
            return "photos/photo_from_desk";
        }
        DeskOwnerMapping.Permission permission = desksOwnerService.getDeskOwnerPermission(deskId, getCurrentUser().getId());

        Page<Photo> photos = desksPhotoService.getPhotosFromDesk(deskId, getCurrentUser().getId(), page, pageSize);
        model.addAttribute("canManagePhoto", permission.canAddPhoto() && permission.canRemovePhoto());
        model.addAttribute("photos", photos.getContent());
        model.addAttribute("users", usersService.getUsernamesMap(photos.map(Photo::getOwnerId).getContent()));
        model.addAttribute("pages", desksPhotoService.getPageNumber(deskId, pageSize));
        model.addAttribute("userId", getCurrentUser().getId());
        return "photos/photo_from_desk";
    }

    @GetMapping("/photo")
    public String viewPhoto(@RequestParam int photoId, Model model) throws PhotoNotFoundException {
        List<Tag> tags = tagsPhotoService.getTagsIdsForPhoto(photoId);
        Photo photo = photoService.getPhotoById(photoId);
        model.addAttribute("userId", getCurrentUser().getId());
        model.addAttribute("path", photo.getPath());
        model.addAttribute("owner", usersService.getUsername(photo.getOwnerId()));
        model.addAttribute("tags", tags.stream().map(Tag::getText).toList());
        return "photos/photo";
    }

    @GetMapping("/photo/upload")
    public String uploadPhoto(Model model, @RequestParam(required = false) String error, @RequestParam(required = false) String description, @RequestParam(required = false) MultipartFile photo) {
        model.addAttribute("userId", getCurrentUser().getId());
        model.addAttribute("description", description);
        model.addAttribute("photo", photo);
        return "photos/upload_photo";
    }

    @PostMapping("perform_photo_upload")
    public void performPhotoUpload(@RequestParam String description, @RequestParam int ownerId, @RequestParam MultipartFile photo, HttpServletResponse response) throws IOException, UserNotFoundException, IncorrectPhotoDescriptionFormat {
        try {
            String path = photoStorageService.uploadPhoto(photo);
            int id = photoService.createPhoto(description, path, ownerId);
            response.sendRedirect("/photo?photoId=" + id);
        } catch (Exception e) {
            response.sendRedirect("/photo/upload?error=" + e.getMessage() + "&description=" + description);
        }
    }
}
