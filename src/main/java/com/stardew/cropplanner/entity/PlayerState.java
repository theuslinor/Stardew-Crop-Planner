package com.stardew.cropplanner.entity;

import com.stardew.cropplanner.enums.Profession;
import com.stardew.cropplanner.enums.Season;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PlayerState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer farmingLevel;
    private Integer currentGold;
    private Integer currentDay;

    @Enumerated(EnumType.STRING)
    private Season currentSeason;

    @Enumerated(EnumType.STRING)
    private Profession profession;
}
