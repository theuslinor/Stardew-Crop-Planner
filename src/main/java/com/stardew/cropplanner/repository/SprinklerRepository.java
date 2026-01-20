package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.Sprinkler;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SprinklerRepository extends JpaRepository<Sprinkler, Long> {
    Optional<Sprinkler> findByNameContainingIgnoreCase(String name);
}
