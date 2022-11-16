package com.main.photoapp.models.Tag.PhotoMapping;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "tags_photos")
@IdClass(TagPhotoMappingId.class)
public class TagPhotoMapping {

    @Id
    @Getter @Setter
    private int photoId;

    @Id
    @Getter @Setter
    private int tagId;

    public TagPhotoMapping() {}

    public TagPhotoMapping(int photoId, int tagId) {
        this.photoId = photoId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPhotoMapping that = (TagPhotoMapping) o;
        return photoId == that.photoId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(photoId, tagId);
    }
}
