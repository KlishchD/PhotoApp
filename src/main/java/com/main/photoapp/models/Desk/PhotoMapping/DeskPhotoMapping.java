package com.main.photoapp.models.Desk.PhotoMapping;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "desks_photos")
@IdClass(DeskPhotoMappingId.class)
@Entity
public class DeskPhotoMapping implements Serializable {

    @Id
    @Column(name = "desk_id")
    @Getter @Setter
    private int deskId;

    @Id
    @Column(name = "photo_id")
    @Getter @Setter
    private int photoId;


    public DeskPhotoMapping() {
    }

    public DeskPhotoMapping(int deskId, int photoId) {
        this.deskId = deskId;
        this.photoId = photoId;
    }
}
