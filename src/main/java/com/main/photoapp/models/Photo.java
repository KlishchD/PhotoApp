package com.main.photoapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @Column(name = "description", nullable = false)
    @Getter @Setter
    private String description;

    @Column(name = "path", nullable = false, unique = true)
    @Getter @Setter
    private String path;

    @Column(name = "owner_id", nullable = false)
    @Getter @Setter
    private int ownerId;

    public Photo(){

    }

    public Photo(String description, String path, int ownerId) {
        this.description = description;
        this.path = path;
        this.ownerId = ownerId;
    }
}