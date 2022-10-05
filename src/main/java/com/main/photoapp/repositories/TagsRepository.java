package com.main.photoapp.repositories;

import com.main.photoapp.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tag, Integer> {

    boolean existsByText(String text);

}
