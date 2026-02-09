package com.stardew.cropplanner.controller;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.entity.FonteSemente;
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
            @RequestParam Long jogadorId,
            @RequestParam(defaultValue = "0") Integer limiteSoloManual) {

        EstadoJogador jogador = estadoJogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        int limiteSoloEfetivo = (limiteSoloManual > 0) ? limiteSoloManual : 100;

        int diasRestantes = 28 - jogador.getDiaAtual();

        return culturaRepository.findByEstacao(jogador.getEstacaoAtual()).stream()
                .map(cultura -> {
                    CulturaRetornoDTO dto = servicoLucro.calcularRetorno(cultura, jogador, diasRestantes);

                    int precoSemente = cultura.getFontesPreco().stream()
                            .mapToInt(FonteSemente::getPreco).min().orElse(0);
                    
                    int qtd = servicoLucro.calcularQuantidadeMaxima(precoSemente, jogador.getOuroDisponivel(), limiteSoloEfetivo);

                    dto.setQuantidadeSementes(qtd);
                    dto.setCustoTotalInvestimento(qtd * precoSemente);
                    dto.setLucroTotalProjeto(Math.round((dto.getLucroTotal() * qtd) * 100.0) / 100.0);

                    dto.setMensagem(dto.getMensagem() + " | Espaço Total: " + limiteSoloEfetivo + " tiles");

                    return dto;
                })
                .sorted((c1, c2) -> c2.getLucroTotalProjeto().compareTo(c1.getLucroTotalProjeto()))
                .collect(Collectors.toList());
    }
}