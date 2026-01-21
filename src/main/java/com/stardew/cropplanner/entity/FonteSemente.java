package com.stardew.cropplanner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FonteSemente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String localVenda;
    private Integer preco;

    @ManyToOne
    @JoinColumn(name = "cultura_id")
    @JsonIgnore
    private Cultura cultura;
}
