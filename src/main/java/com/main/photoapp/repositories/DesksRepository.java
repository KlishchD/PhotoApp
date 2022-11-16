package com.main.photoapp.repositories;

import com.main.photoapp.models.Desk.Desk;
import com.main.photoapp.models.Desk.OwnersMapping.DeskOwnerMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Integer> {

    List<Desk> findAllByType(Desk.DeskType type);

}
