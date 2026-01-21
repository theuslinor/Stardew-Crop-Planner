package com.stardew.cropplanner.controller;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.repository.CulturaRepository;
import com.stardew.cropplanner.repository.EstadoJogadorRepository;
import com.stardew.cropplanner.service.ServicoLucro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/otimizar")
public class ControllerOtimizacao {

    @Autowired
    private ServicoLucro servicoLucro;

    @Autowired
    private CulturaRepository culturaRepository;

    @Autowired
    private EstadoJogadorRepository estadoJogadorRepository;

    @GetMapping("/melhores-culturas")
    public List<CulturaRetornoDTO> obterMelhoresCulturas(
            @RequestParam Long jogadorId) {

        EstadoJogador jogador = estadoJogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        List<Cultura> culturas = culturaRepository.findByEstacao(jogador.getEstacaoAtual());
        int diasRestantes = 28 - jogador.getDiaAtual();

        return culturas.stream()
                .map(cultura -> servicoLucro.calcularRetorno(cultura, jogador, diasRestantes))
                .collect(Collectors.toList());
    }
}