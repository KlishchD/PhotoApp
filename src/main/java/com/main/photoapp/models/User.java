package com.main.photoapp.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    @Column(name = "username")
    @Getter @Setter
    private String nickname;

    @Column(name = "email")
    @Getter @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @Getter @Setter
    private String password;

    @Column(name = "enabled", nullable = false)
    @Getter @Setter
    boolean enabled = true;

    public User(){

    }

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public UserDetails getSecurityUserDetails() {
        return org.springframework.security.core.userdetails.User
                .withUsername(nickname)
                .password(password)
                .roles("USER").build();
    }
}
