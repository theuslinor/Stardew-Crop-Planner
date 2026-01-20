package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.PlayerState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerStateRepository extends JpaRepository<PlayerState, Long> {
    // Geralmente teremos apenas um PlayerState no banco,
    // mas o JpaRepository já nos dá o findById(id) por padrão.
}
