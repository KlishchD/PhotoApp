package com.main.photoapp.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "tags")
@Entity
public class Tag {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private int id;

    @Column(name = "tag", unique = true)
    @Getter
    @Setter
    private String tag;

    public Tag(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }
    public Tag(String tag) {
        this.tag = tag;
    }
    public Tag() {

    }
}
