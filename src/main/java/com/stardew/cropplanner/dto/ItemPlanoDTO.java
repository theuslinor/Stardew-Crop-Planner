package com.stardew.cropplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemPlanoDTO {
    private String nomeCultura;
    private Integer quantidade;
    private Double lucroEstimadoCultura;
}
