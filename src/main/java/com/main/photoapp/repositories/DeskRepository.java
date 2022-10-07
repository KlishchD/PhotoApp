package com.main.photoapp.repositories;

import com.main.photoapp.models.Desk.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Integer> {
}
