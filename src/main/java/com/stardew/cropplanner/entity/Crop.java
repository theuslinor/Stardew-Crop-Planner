package com.stardew.cropplanner.entity;

import com.stardew.cropplanner.enums.Season;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Preços para diferentes qualidades
    private Integer priceNormal;
    private Integer priceSilver;
    private Integer priceGold;
    private Integer priceIridium;

    private boolean recurring; // Se continua produzindo ápos a colheita
    private Integer growthTime; // Dias para crescer
    private Integer regrowhTime; // Dias para produzir novamente (se recorrente)

    @Enumerated(EnumType.STRING)
    private Season season;
}
