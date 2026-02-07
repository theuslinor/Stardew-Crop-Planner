package com.stardew.cropplanner.controller;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.repository.CulturaRepository;
import com.stardew.cropplanner.repository.EstadoJogadorRepository;
import com.stardew.cropplanner.service.ServicoLucro;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otimizar")
public class ControllerOtimizacao {

    private final ServicoLucro servicoLucro;
    private final CulturaRepository culturaRepository;
    private final EstadoJogadorRepository estadoJogadorRepository;

    @GetMapping("/melhores-culturas")
    public List<CulturaRetornoDTO> obterMelhoresCulturas(
            @RequestParam Long jogadorId) {

        EstadoJogador jogador = estadoJogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        int diasRestantes = 28 - jogador.getDiaAtual();

        return culturaRepository.findByEstacao(jogador.getEstacaoAtual()).stream()
                .map(cultura -> servicoLucro.calcularRetorno(cultura, jogador, diasRestantes))
                // ORDENAÇÃO: Comparamos o lucro diário da forma decrescente
                .sorted((c1, c2) -> c2.getLucroDiario().compareTo(c1.getLucroDiario()))
                .collect(Collectors.toList());
    }
}