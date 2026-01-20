package com.stardew.cropplanner.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CulturaRetornoDTO {
    private String nomeCultura;
    private Double lucroTotal;
    private Double porcentagemRetorno;
    private Integer totalColheitas;
    private String mensagem;
}