package com.main.photoapp.controllers.internal.Tags;

import com.main.photoapp.exceptions.NoSuchTagOnPhoto;
import com.main.photoapp.exceptions.TagIsAlreadyAdded;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.SearchRequest;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.services.Tags.TagsPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagsPhotoController {
    private final TagsPhotoService service;

    @Autowired
    public TagsPhotoController(TagsPhotoService service) {
        this.service = service;
    }

    @PostMapping("photo/add/tag")
    public void addTagToPhoto(int tagId, int photoId, int userId) throws TagNotFoundException, UserNotFoundException, TagIsAlreadyAdded {
        service.addTagToPhoto(tagId, photoId, userId);
    }

    @PostMapping("photo/remove/tag")
    public void removeTagFromPhoto(int tagId, int photoId, int userId) throws TagNotFoundException, UserNotFoundException, NoSuchTagOnPhoto {
        service.removeTagFromPhoto(tagId, photoId, userId);
    }

    @GetMapping("photo/get/tag")
    public List<Tag> getTagsIdsForPhoto(int photoId) {
        return service.getTagsIdsForPhoto(photoId);
    }

    @GetMapping("photo/exists/tag")
    public boolean isTagAttachedToPhoto(int photoId, int tagId) throws TagNotFoundException {
        return service.isTagAttachedToPhoto(photoId, tagId);
    }

    @PostMapping("photo/find")
    public List<Integer> findPhotoIds(@RequestBody SearchRequest request) {
        return service.findPhotoIds(request.getTagIds(), request.getPage(), request.getPageSize()).getContent();
    }
}
