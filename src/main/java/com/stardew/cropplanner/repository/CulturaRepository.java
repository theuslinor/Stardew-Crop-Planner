package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.enums.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CulturaRepository extends JpaRepository<Cultura, Long> {

    List<Cultura> findByEstacao(Estacao estacao);

    Optional<Cultura> findByNomeIgnoreCase(String nome);

    List<Cultura> findByRecorrenteTrue();
}