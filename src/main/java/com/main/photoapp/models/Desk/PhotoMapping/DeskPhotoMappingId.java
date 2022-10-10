package com.main.photoapp.models.Desk.PhotoMapping;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public class DeskPhotoMappingId implements Serializable {
    @Getter @Setter
    private int deskId;

    @Getter @Setter
    private int photoId;


    public DeskPhotoMappingId() {}
    public DeskPhotoMappingId(int deskId, int photoId) {
        this.deskId = deskId;
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeskPhotoMappingId that = (DeskPhotoMappingId) o;
        return deskId == that.deskId && photoId == that.photoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deskId, photoId);
    }
}
