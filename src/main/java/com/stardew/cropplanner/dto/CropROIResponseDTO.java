package com.stardew.cropplanner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CropROIResponseDTO {
    private String cropName;
    private Double totalProfit;
    private Double roiPercentage;
    private Integer totalHarvest;
    private String message; // Ex: "Melhor opção para o final do mês"
}
