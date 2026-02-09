package com.stardew.cropplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "inventario_aspersor")
public class ItemInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jogador_id")
    @JsonIgnore
    private EstadoJogador jogador;

    @ManyToOne
    @JoinColumn(name = "aspersor_id")
    private Aspersor aspersor;

    private Integer quantidade;
}
