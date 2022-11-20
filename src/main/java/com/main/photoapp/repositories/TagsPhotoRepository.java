package com.main.photoapp.repositories;

import com.main.photoapp.models.Tag.PhotoMapping.TagPhotoMapping;
import com.main.photoapp.models.Tag.PhotoMapping.TagPhotoMappingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagsPhotoRepository extends JpaRepository<TagPhotoMapping, TagPhotoMappingId> {

    List<TagPhotoMapping> findAllByPhotoId(int photoId);

    @Query("select m.photoId from TagPhotoMapping m where m.tagId in ?1 group by m.photoId having count(m.tagId) >= ?2")
    Page<Integer> findPhotoIds(List<Integer> tagsIds, long minimalTagsNumber, Pageable pageable);

}
