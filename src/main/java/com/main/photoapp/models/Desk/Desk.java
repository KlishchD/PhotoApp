package com.main.photoapp.models.Desk;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Table(name = "desks")
@Entity
public class Desk {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Column(name = "description", nullable = false)
    @Getter @Setter
    private String description;

    @Column(name = "type", nullable = false)
    @Getter @Setter
    private DeskType type;

    public Desk() {
    }

    public Desk(String name, String description) {
        this.name = name;
        this.description = description;
        this.type = DeskType.PUBLIC;
    }

    public Desk(String name, String description, DeskType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public enum DeskType {
        PUBLIC,
        PRIVATE
    }
}
