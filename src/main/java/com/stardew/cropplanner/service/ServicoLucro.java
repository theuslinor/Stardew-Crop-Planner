package com.stardew.cropplanner.service;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.entity.FonteSemente;
import com.stardew.cropplanner.enums.Profissao;
import org.springframework.stereotype.Service;

@Service
public class ServicoLucro {

    public CulturaRetornoDTO calcularRetorno(Cultura cultura, EstadoJogador jogador, Integer diasRestantes) {
        int colheitas = 0;

        // Busca o menor preço na lista de fontes, Se ea lista estiver vazia, assume 0
        int menorCustoSemente = 0;
        if (cultura.getFontesPreco() != null && !cultura.getFontesPreco().isEmpty()){
            menorCustoSemente = cultura.getFontesPreco().stream()
                    .mapToInt(FonteSemente::getPreco)
                    .min()
                    .orElse(0);
        }

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
        double lucroTotal = receitaTotal - menorCustoSemente;
        double roi = (menorCustoSemente > 0) ? (lucroTotal / menorCustoSemente) * 100 : 0;

        return CulturaRetornoDTO.builder()
                .nomeCultura(cultura.getNome())
                .lucroTotal(lucroTotal)
                .porcentagemRetorno(roi)
                .totalColheitas(colheitas)
                .mensagem("Custo semente: " + menorCustoSemente + "g (Melhor preço encontrado)")
                .build();
    }
}