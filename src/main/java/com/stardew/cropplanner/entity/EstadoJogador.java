package com.stardew.cropplanner.entity;

import com.stardew.cropplanner.enums.Estacao;
import com.stardew.cropplanner.enums.Profissao;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EstadoJogador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nivelCultivo;
    private Integer ouroDisponivel;
    private Integer diaAtual;
    private Integer espacoManual;

    @Enumerated(EnumType.STRING)
    private Estacao estacaoAtual;

    @Enumerated(EnumType.STRING)
    private Profissao profissao;

}