package com.stardew.cropplanner.repository;

import com.stardew.cropplanner.entity.Crop;
import com.stardew.cropplanner.enums.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findBySeason(Season season);
    Optional<Crop> findByNameIgnoreCase(String name);
    List<Crop> findByRecurringTrue();
}
