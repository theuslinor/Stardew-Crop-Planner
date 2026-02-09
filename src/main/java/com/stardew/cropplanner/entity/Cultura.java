package com.stardew.cropplanner.entity;

import com.stardew.cropplanner.enums.Estacao;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Cultura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer precoNormal;
    private Integer precoPrata;
    private Integer precoOuro;
    private Integer precoIridium;
    private boolean recorrente;
    private Integer tempoCrescimento;
    private Integer tempoRebrota;

    @OneToMany(mappedBy = "cultura", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FonteSemente> fontesPreco;

    @Enumerated(EnumType.STRING)
    private Estacao estacao;

    private Integer anoDisponivel;
    private Integer diaDisponivel;
    private boolean requerDesbloqueio;
    private String localVendaPadrao;
}
