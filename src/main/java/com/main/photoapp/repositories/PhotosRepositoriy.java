package com.main.photoapp.repositories;

import com.main.photoapp.models.Photo;
import com.main.photoapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotosRepositoriy extends JpaRepository<Photo, Integer> {
    boolean existsByDescription(String description);

    boolean existsByOwnerId(int ownerId);

    Optional<Photo> findById(int id);
}
