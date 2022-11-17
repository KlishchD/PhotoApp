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

    @Column(name = "paths", nullable = false, unique = true)
    @Getter @Setter
    private String paths;

    @Column(name = "owner_id", nullable = false)
    @Getter @Setter
    private int ownerId;

    public Photo(){

    }

    public Photo(String description, String paths, int ownerId) {
        this.description = description;
        this.paths = paths;
        this.ownerId = ownerId;
    }
}