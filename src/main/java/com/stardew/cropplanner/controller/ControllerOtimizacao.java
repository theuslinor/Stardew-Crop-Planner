package com.stardew.cropplanner.controller;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.dto.PlanoPlantioDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.entity.FonteSemente;
import com.stardew.cropplanner.repository.CulturaRepository;
import com.stardew.cropplanner.repository.EstadoJogadorRepository;
import com.stardew.cropplanner.service.ServicoLucro;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otimizar")
@CrossOrigin(origins = "http://localhost:4200")
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

        // RF04 - Calcula o solo disponível (Soma manual + aspersores)
        int limiteSoloEfetivo = (limiteSoloManual > 0) ? limiteSoloManual
                : (jogador.getEspacoManual() + servicoLucro.calcularLimiteSoloPorAspersor(jogador));

        int diasRestantes = 28 - jogador.getDiaAtual();

        return culturaRepository.findByEstacao(jogador.getEstacaoAtual()).stream()
                // Filtro de Calendário (Morango dia 13, Alho Ano 2, etc)
                .filter(cultura -> servicoLucro.estaDisponivelParaCompra(cultura, jogador))
                .map(cultura -> {
                    CulturaRetornoDTO dto = servicoLucro.calcularRetorno(cultura, jogador, diasRestantes);

                    // Busca o menor preço de semente
                    int precoSemente = cultura.getFontesPreco().stream()
                            .mapToInt(FonteSemente::getPreco).min().orElse(0);

                    int qtd;
                    String avisoEstoque = "";
                    if (precoSemente > 0) {
                        qtd = servicoLucro.calcularQuantidadeMaxima(precoSemente, jogador.getOuroDisponivel(), limiteSoloEfetivo);
                    } else {
                        // Se o preço é 0, o sistema não sabe quantas você achou no mapa.
                        // Assumimos 0 para não "poluir" o lucro do projeto com dados fictícios.
                        qtd = 0;
                        avisoEstoque = " (Requer estoque manual)";
                    }

                    dto.setQuantidadeSementes(qtd);
                    dto.setCustoTotalInvestimento(qtd * precoSemente);
                    dto.setLucroTotalProjeto(Math.round((dto.getLucroTotal() * qtd) * 100.0) / 100.0);

                    dto.setMensagem(dto.getMensagem() + " | Solo Utilizado: " + limiteSoloEfetivo + " tiles" + avisoEstoque);

                    return dto;
                })
                .sorted((c1, c2) -> c2.getLucroTotalProjeto().compareTo(c1.getLucroTotalProjeto()))
                .collect(Collectors.toList());
    }

    @GetMapping("/plano-misto")
    public PlanoPlantioDTO obterPlanoMisto(
            @RequestParam Long jogadorId,
            @RequestParam(required = false) List<Long> idsIgnorados) {

        List<CulturaRetornoDTO> rankingCompleto = obterMelhoresCulturas(jogadorId, 0);

        List<CulturaRetornoDTO> rankingFiltrado = rankingCompleto.stream()
                .filter(dto -> {
                    if (idsIgnorados == null || idsIgnorados.isEmpty()) return true;

                    Cultura c = culturaRepository.findByNomeIgnoreCase(dto.getNomeCultura()).orElse(null);
                    return c == null || !idsIgnorados.contains(c.getId());
                })
                .collect(Collectors.toList());

        EstadoJogador jogador = estadoJogadorRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        int espacoTotal = jogador.getEspacoManual() + servicoLucro.calcularLimiteSoloPorAspersor(jogador);

        return servicoLucro.gerarPlanoMix(rankingFiltrado, jogador.getOuroDisponivel(), espacoTotal);
    }
}