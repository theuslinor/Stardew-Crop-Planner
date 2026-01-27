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

        // 1. MODIFICADORES DE VELOCIDADE (RF02)
        // Calcula o tempo de crescimento real considerando a profissão Agricultor
        int tempoCrescimentoReal = calcularTempoCrescimentoReal(cultura.getTempoCrescimento(), jogador);

        // 2. GESTÃO DE CUSTOS (RF11)
        // Busca o menor preço de semente disponível no banco de dados. Caso vazio, assume 0.
        int menorCustoSemente = 0;
        if (cultura.getFontesPreco() != null && !cultura.getFontesPreco().isEmpty()) {
            menorCustoSemente = cultura.getFontesPreco().stream()
                    .mapToInt(FonteSemente::getPreco)
                    .min()
                    .orElse(0);
        }

        // 3. LÓGICA DE CRESCIMENTO E COLHEITAS (RF07)
        int colheitas = 0;
        int diasOcupados = 0;

        if (diasRestantes >= tempoCrescimentoReal) {
            if (cultura.isRecorrente()) {
                // Plantas que produzem continuamente (ex: Morango)
                colheitas = 1;
                if (cultura.getTempoRebrota() != null && cultura.getTempoRebrota() > 0) {
                    int diasAposPrimeira = diasRestantes - tempoCrescimentoReal;
                    colheitas += (diasAposPrimeira / cultura.getTempoRebrota());
                }
                diasOcupados = diasRestantes; //Planta fica lá o mes todo
            } else {
                colheitas = 1;
                diasOcupados = tempoCrescimentoReal;
            }
        }

        // 4. MODIFICADORES DE VENDA (RF01)
        // Aplica o bônus de 10% da profissão Cultivador (Tiller)
        int precoBase = cultura.getPrecoNormal();
        int precoVenda = (jogador.getProfissao() == Profissao.CULTIVADOR)
                ? (precoBase * 110) / 100
                : precoBase;

        // 5. CÁLCULOS FINANCEIROS FINAIS
        double receitaTotal = (double) colheitas * precoVenda;
        double custoTotalSementes = (double) menorCustoSemente;
        double lucroTotal = receitaTotal - custoTotalSementes;

        double lucroDiarioBruto = (diasOcupados > 0) ? (lucroTotal / diasOcupados) : 0;
        double lucroDiario = Math.round(lucroDiarioBruto * 100) / 100.0;
        double roi = (custoTotalSementes > 0) ? (lucroTotal / custoTotalSementes) * 100 : 0;

        // 6. CONSTRUÇÃO DA RESPOSTA (DTO)
        return CulturaRetornoDTO.builder()
                .nomeCultura(cultura.getNome())
                .lucroTotal(lucroTotal)
                .lucroDiario(lucroDiario)
                .porcentagemRetorno(roi)
                .totalColheitas(colheitas)
                .mensagem("Semente: " + menorCustoSemente + "g | Crescimento Real: " + tempoCrescimentoReal + " dias")
                .build();
    }

    /**
     * Calcula o tempo de crescimento real baseado nos bônus do jogador (RF02/RF03).
     */
    private int calcularTempoCrescimentoReal(int diasOriginais, EstadoJogador jogador) {
        double bonusVelocidade = 0.0;

        if (jogador.getProfissao() == Profissao.AGRICULTOR) {
            bonusVelocidade = 0.10; // Bônus de 10% de velocidade
        }

        // Teto(Dias / (1 + Bônus)) conforme especificado no PRD
        return (int) Math.ceil(diasOriginais / (1.0 + bonusVelocidade));
    }
}