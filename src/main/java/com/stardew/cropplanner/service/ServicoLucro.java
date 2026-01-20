package com.stardew.cropplanner.service;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.enums.Profissao;
import org.springframework.stereotype.Service;

@Service
public class ServicoLucro {

    public CulturaRetornoDTO calcularRetorno(Cultura cultura, EstadoJogador jogador, Integer custoSemente, Integer diasRestantes) {
        int colheitas = 0;

        // 1. Calcular colheitas
        if (diasRestantes >= cultura.getTempoCrescimento()) {
            colheitas = 1;
            if (cultura.isRecorrente() && cultura.getTempoRebrota() != null && cultura.getTempoRebrota() > 0) {
                int diasAposPrimeiraColheita = diasRestantes - cultura.getTempoCrescimento();
                colheitas += (diasAposPrimeiraColheita / cultura.getTempoRebrota());
            }
        }

        // 2. Preço com Profissão Cultivador (Tiller)
        int precoBase = cultura.getPrecoNormal();
        int precoVenda;
        if (jogador.getProfissao() == Profissao.CULTIVADOR) {
            precoVenda = (precoBase * 110) / 100;
        } else {
            precoVenda = precoBase;
        }

        // 3. Cálculos
        double receitaTotal = (double) colheitas * precoVenda;
        double lucroTotal = receitaTotal - custoSemente;
        double roi = (custoSemente > 0) ? (lucroTotal / custoSemente) * 100 : 0;

        return CulturaRetornoDTO.builder()
                .nomeCultura(cultura.getNome())
                .lucroTotal(lucroTotal)
                .porcentagemRetorno(roi)
                .totalColheitas(colheitas)
                .mensagem(colheitas == 0 ? "Sem tempo para colheita!" : "Cálculo otimizado")
                .build();
    }
}