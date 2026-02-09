package com.stardew.cropplanner.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlanoPlantioDTO {
    private Double lucroTotalEstimado;
    private Integer ouroRestante;
    private Integer espacoRestante;
    private List<ItemPlanoDTO> itens;
}

