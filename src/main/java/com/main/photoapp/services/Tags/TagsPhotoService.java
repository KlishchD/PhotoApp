package com.main.photoapp.services.Tags;

import com.main.photoapp.exceptions.NoSuchTagOnPhoto;
import com.main.photoapp.exceptions.TagIsAlreadyAdded;
import com.main.photoapp.exceptions.TagNotFoundException;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.Tag.PhotoMapping.TagPhotoMapping;
import com.main.photoapp.models.Tag.PhotoMapping.TagPhotoMappingId;
import com.main.photoapp.models.Tag.Tag;
import com.main.photoapp.repositories.TagsPhotoRepository;
import com.main.photoapp.repositories.TagsRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagsPhotoService {

    @Autowired
    private TagsService tags;

    @Autowired
    private UsersService users;

    @Autowired
    private TagsPhotoRepository mapping;

    public Page<Integer> findPhotoIds(List<Integer> tagsIds, int page, int pageSize) {
        System.out.println("------------------------------------------------------------------");
        System.out.println(tagsIds);
        System.out.println((int) (tagsIds.size() * 0.85));
        System.out.println("------------------------------------------------------------------");
        return mapping.findPhotoIds(tagsIds, (int) (tagsIds.size() * 0.85), Pageable.ofSize(pageSize).withPage(page));
    }

    public void addTagToPhoto(int tagId, int photoId, int userId) throws TagNotFoundException, UserNotFoundException, TagIsAlreadyAdded {
        if (tags.tagNotExists(tagId)) throw new TagNotFoundException(tagId);
        // TODO: Photo Existence check
        if (users.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (photoHasTag(photoId, tagId)) throw new TagIsAlreadyAdded(tagId, photoId);
        // TODO: User is photo owner check
        mapping.save(new TagPhotoMapping(photoId, tagId));
    }

    public void removeTagFromPhoto(int tagId, int photoId, int userId) throws TagNotFoundException, UserNotFoundException, NoSuchTagOnPhoto {
        if (tags.tagNotExists(tagId)) throw new TagNotFoundException(tagId);
        // TODO: Photo Existence check
        if (users.userNotExists(userId)) throw new UserNotFoundException(userId);
        if (photoHasNotTag(photoId, tagId)) throw new NoSuchTagOnPhoto(tagId, photoId);
        // TODO: User is photo owner check
        mapping.delete(new TagPhotoMapping(photoId, tagId));
    }

    public List<Tag> getTagsIdsForPhoto(int photoId) {
        // TODO: Photo Existence check
        return mapping.findAllByPhotoId(photoId).stream()
                .map(TagPhotoMapping::getTagId)
                .map(tags::getTagById)
                .collect(Collectors.toList());
    }

    public boolean isTagAttachedToPhoto(int photoId, int tagId) throws TagNotFoundException {
        // TODO: Photo Existence check
        if (tags.tagNotExists(tagId)) throw new TagNotFoundException(tagId);
        return photoHasTag(photoId, tagId);
    }

    private boolean photoHasNotTag(int photoId, int tagId) {
        return !photoHasTag(photoId, tagId);
    }

    protected boolean photoHasTag(int photoId, int tagId) {
        return mapping.existsById(new TagPhotoMappingId(photoId, tagId));
    }
}
