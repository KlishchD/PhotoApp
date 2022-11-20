package com.main.photoapp.repositories;

import com.main.photoapp.models.Tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsRepository extends JpaRepository<Tag, Integer> {

    boolean existsByText(String text);

    @Query("select t from Tag t where t.text like concat('%', ?1, '%')")
    List<Tag> findByTextLike(String text);

}
