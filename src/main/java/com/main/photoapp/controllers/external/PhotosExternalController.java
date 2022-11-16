package com.main.photoapp.controllers.external;

import com.main.photoapp.exceptions.DeskNotFoundException;
import com.main.photoapp.exceptions.NotEnoughPermissionsException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.services.Desks.DesksOwnerService;
import com.main.photoapp.services.Desks.DesksPhotoService;
import com.main.photoapp.services.Tags.TagsPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ControllerAdvice
@Controller
public class PhotosExternalController extends ExternalControllerBase {
    @Autowired
    private DesksPhotoService desksPhotoService;

    @Autowired
    private DesksOwnerService desksOwnerService;

    @Autowired
    private TagsPhotoService tagsPhotoService;
    

    @GetMapping("/view_photos")
    public String viewPhotos(@RequestParam int deskId, @RequestParam int page, @RequestParam(defaultValue = "2") int pageSize, Model model) throws DeskNotFoundException, UserNotFoundException, NotEnoughPermissionsException {
        if (!desksOwnerService.canUserAccessDesk(deskId, getCurrentUser().getId())) {
            model.addAttribute("error", "Not enough permissions");
            return "photos/photo_from_desk";
        }
        DeskOwnerMapping.Permission permission = desksOwnerService.getDeskOwnerPermission(deskId, getCurrentUser().getId());

        Page<Integer> photos = desksPhotoService.getPhotosFromDesk(deskId, getCurrentUser().getId(), page, pageSize);
        model.addAttribute("canManagePhoto", permission.canAddPhoto() && permission.canRemovePhoto());
        model.addAttribute("photos", photos.getContent());
        model.addAttribute("pages", desksPhotoService.getPageNumber(deskId, pageSize));
        model.addAttribute("userId", getCurrentUser().getId());
        return "photos/photo_from_desk";
    }

    @GetMapping("/photo")
    public String viewPhoto(@RequestParam int photoId, Model model) {
        List<Tag> tags = tagsPhotoService.getTagsIdsForPhoto(photoId);
        model.addAttribute("userId", getCurrentUser().getId());
        model.addAttribute("tags", tags.stream().map(Tag::getText).toList());
        return "photos/photo";
    }

    @GetMapping("/photo/upload")
    public String uploadPhoto(Model model) {
        model.addAttribute("userId", getCurrentUser().getId());
        return "photos/upload_photo";
    }

    @PostMapping("perform_photo_upload")
    public void performPhotoUpload(@RequestParam String description, @RequestParam byte[] photo, @RequestParam int ownerId) {

    }
}
