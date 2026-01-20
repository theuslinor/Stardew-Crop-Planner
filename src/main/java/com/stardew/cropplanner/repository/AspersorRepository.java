package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.Aspersor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AspersorRepository extends JpaRepository<Aspersor, Long> {

    Optional<Aspersor> findByNomeContainingIgnoreCase(String nome);
}