package com.stardew.cropplanner.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CulturaRetornoDTO {
    private String nomeCultura;
    private Double lucroTotal;
    private Double lucroDiario;
    private Double porcentagemRetorno;
    private Integer totalColheitas;
    private String mensagem;
}