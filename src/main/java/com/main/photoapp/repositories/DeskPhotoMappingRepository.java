package com.main.photoapp.repositories;

import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMapping;
import com.main.photoapp.models.Desk.PhotoMapping.DeskPhotoMappingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

public interface DeskPhotoMappingRepository extends JpaRepository<DeskPhotoMapping, DeskPhotoMappingId> {

    void deleteByDeskIdAndPhotoId(int deskId, int photoId);

    Page<DeskPhotoMapping> findAllByDeskId(int deskId, Pageable pageable);

}
