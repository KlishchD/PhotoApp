package com.main.photoapp.repositories;

import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMappingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;

import java.util.List;
import java.util.Optional;

public interface DeskOwnerRepository extends JpaRepository<DeskOwnerMapping, DeskOwnerMappingId> {

    List<DeskOwnerMapping> findByDeskId(int deskId);

    void deleteAllByDeskId(int deskId);

    List<DeskOwnerMapping> findAllByPermission(DeskOwnerMapping.Permission permission);

    List<DeskOwnerMapping> findByDeskIdAndPermission(int deskId, DeskOwnerMapping.Permission permission);

    void deleteByDeskIdAndUserId(int deskId, int userId);

    boolean existsByDeskIdAndUserId(int deskId, int userId);

    Optional<DeskOwnerMapping> findByDeskIdAndUserId(int deskId, int userId);

    List<DeskOwnerMapping> findAllByUserIdAndPermission(int userId, DeskOwnerMapping.Permission permission);

    List<DeskOwnerMapping> findAllByUserId(int userId);

}
