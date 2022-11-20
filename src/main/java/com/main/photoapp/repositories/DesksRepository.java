package com.main.photoapp.repositories;

import com.main.photoapp.models.Desk.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesksRepository extends JpaRepository<Desk, Integer> {

    List<Desk> findAllByType(Desk.DeskType type);

}
