package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.EstadoJogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoJogadorRepository extends JpaRepository<EstadoJogador, Long> {
}