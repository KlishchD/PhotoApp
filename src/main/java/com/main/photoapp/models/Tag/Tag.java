package com.main.photoapp.models.Tag;

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
    @Getter
    private int id;

    @Column(name = "text", unique = true)
    @Getter
    @Setter
    private String text;

    public Tag(int id, String text) {
        this.id = id;
        this.text = text;
    }
    public Tag(String text) {
        this.text = text;
    }
    public Tag() {

    }
}
