package com.main.photoapp.models.Tag.PhotoMapping;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public class TagPhotoMappingId implements Serializable {

    @Getter @Setter
    private int photoId;

    @Getter @Setter
    private int tagId;

    public TagPhotoMappingId() {}

    public TagPhotoMappingId(int photoId, int tagId) {
        this.photoId = photoId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPhotoMappingId that = (TagPhotoMappingId) o;
        return photoId == that.photoId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, tagId);
    }
}
