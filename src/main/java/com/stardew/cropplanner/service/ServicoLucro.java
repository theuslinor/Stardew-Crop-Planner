package com.stardew.cropplanner.service;

import com.stardew.cropplanner.dto.CulturaRetornoDTO;
import com.stardew.cropplanner.dto.ItemPlanoDTO;
import com.stardew.cropplanner.dto.PlanoPlantioDTO;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.entity.EstadoJogador;
import com.stardew.cropplanner.entity.FonteSemente;
import com.stardew.cropplanner.enums.Profissao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoLucro {

    /**
     * Calcula o retorno financeiro detalhado de uma cultura específica.
     */
    public CulturaRetornoDTO calcularRetorno(Cultura cultura, EstadoJogador jogador, Integer diasRestantes) {

        // 1. MODIFICADORES DE VELOCIDADE (RF02)
        int tempoCrescimentoReal = calcularTempoCrescimentoReal(cultura.getTempoCrescimento(), jogador);

        // 2. GESTÃO DE CUSTOS (RF11)
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
                // Plantas recorrentes ocupam o solo até o fim da estação
                colheitas = 1;
                if (cultura.getTempoRebrota() != null && cultura.getTempoRebrota() > 0) {
                    int diasAposPrimeira = diasRestantes - tempoCrescimentoReal;
                    colheitas += (diasAposPrimeira / cultura.getTempoRebrota());
                }
                diasOcupados = diasRestantes;
            } else {
                // Plantas únicas ocupam o solo apenas por um ciclo
                colheitas = 1;
                diasOcupados = tempoCrescimentoReal;
            }
        }

        // 4. CÁLCULO DE QUALIDADE E PREÇO MÉDIO (RF06)
        // Substituímos o preço fixo por uma média ponderada baseada no nível de cultivo
        boolean eCultivador = (jogador.getProfissao() == Profissao.CULTIVADOR);
        double precoVendaMedio = calcularPrecoMedioPonderado(cultura, jogador.getNivelCultivo(), eCultivador);

        // 5. CÁLCULOS FINANCEIROS FINAIS
        double receitaTotal = (double) colheitas * precoVendaMedio;
        double custoInvestimento = (double) menorCustoSemente;
        double lucroTotal = receitaTotal - custoInvestimento;

        // Cálculo de métricas diárias e ROI
        double lucroDiarioBruto = (diasOcupados > 0) ? (lucroTotal / diasOcupados) : 0;

        // Arredondamentos para 2 casas decimais para exibição limpa
        double lucroTotalFinal = Math.round(lucroTotal * 100.0) / 100.0;
        double lucroDiarioFinal = Math.round(lucroDiarioBruto * 100.0) / 100.0;
        double roiFinal = (custoInvestimento > 0) ? Math.round((lucroTotal / custoInvestimento) * 10000.0) / 100.0 : 0;

        // 6. CONSTRUÇÃO DA RESPOSTA (DTO)
        return CulturaRetornoDTO.builder()
                .nomeCultura(cultura.getNome())
                .lucroTotal(lucroTotalFinal)
                .lucroDiario(lucroDiarioFinal)
                .porcentagemRetorno(roiFinal)
                .totalColheitas(colheitas)
                .mensagem("Semente: " + menorCustoSemente + "g | Crescimento Real: " + tempoCrescimentoReal + " dias")
                .build();
    }

    /**
     * Calcula o tempo de crescimento real baseado nos bônus do jogador (RF02).
     */
    private int calcularTempoCrescimentoReal(int diasOriginais, EstadoJogador jogador) {
        double bonusVelocidade = 0.0;
        if (jogador.getProfissao() == Profissao.AGRICULTOR) {
            bonusVelocidade = 0.10;
        }
        return (int) Math.ceil(diasOriginais / (1.0 + bonusVelocidade));
    }

    /**
     * RF06 - Lógica de Probabilidade de Qualidade baseada no Farming Level.
     */
    private double calcularPrecoMedioPonderado(Cultura cultura, int nivelCultivo, boolean eCultivador) {
        // Cálculo de probabilidades baseado no nível (Fórmulas simplificadas do jogo)
        double chanceOuro = Math.min(0.75, (nivelCultivo * 0.07));
        double chancePrata = Math.min(0.75, (nivelCultivo * 0.15));
        double chanceNormal = Math.max(0, 1.0 - chanceOuro - chancePrata);

        // Aplica modificador de profissão Cultivador se ativo
        double pNormal = eCultivador ? (cultura.getPrecoNormal() * 1.1) : cultura.getPrecoNormal();
        double pPrata = eCultivador ? (cultura.getPrecoPrata() * 1.1) : cultura.getPrecoPrata();
        double pOuro = eCultivador ? (cultura.getPrecoOuro() * 1.1) : cultura.getPrecoOuro();

        // Média ponderada pela probabilidade
        return (pNormal * chanceNormal) + (pPrata * chancePrata) + (pOuro * chanceOuro);
    }

    /**
     * RF04 - Calcula a quantidade máxima de sementes que o jogador pode plantar
     * beasedo no ouro disponivel e no limite de espaço fisico.
     */
    public int calcularQuantidadeMaxima(int precoSemente, int ouroDisponivel, int limiteSolo){
        if (precoSemente <= 0) return limiteSolo;

        int maxPorDinheiro = ouroDisponivel / precoSemente;

        // O limite real é menor que o dinheiro e o espaço fisico
        return Math.min(maxPorDinheiro, limiteSolo);
    }

    public int calcularLimiteSoloPorAspersor(EstadoJogador jogador){
        if (jogador.getInventarioAspersores() == null ||jogador.getInventarioAspersores().isEmpty()){
            return 0;
        }

        return jogador.getInventarioAspersores().stream()
                .mapToInt(item -> item.getAspersor().getCapacidade() * item.getQuantidade())
                .sum();
    }

    /**
     * RF05 - Algoritmo Guloso para criar um mix de culturas que maximiza o lucro
     */
    public PlanoPlantioDTO gerarPlanoMix(List<CulturaRetornoDTO> ranking, int ouroDisponivel, int limiteSolo){
        List<ItemPlanoDTO> itensSugerido = new java.util.ArrayList<>();
        int ouroRestante = ouroDisponivel;
        int soloRestante = limiteSolo;
        double lucroTotalPlano = 0;

        // O Ranking já vem ordenado por lucroDiario(melhores primeiro)
        for (CulturaRetornoDTO cultura : ranking){
            if (soloRestante <= 0 || ouroRestante <= 0) break;

            // Recupera o custo de UMA unidade (Custo total / qtd sementes calculada no ranking)
            int custoUnidade = (int) (cultura.getCustoTotalInvestimento() / cultura.getQuantidadeSementes());
            int qtdPorDinheiro = ouroRestante / custoUnidade;
            int qtdFinal = Math.min(qtdPorDinheiro, soloRestante);

            if (custoUnidade > ouroRestante) continue;

            if (qtdFinal > 0){
                itensSugerido.add(new ItemPlanoDTO(
                        cultura.getNomeCultura(),
                        qtdFinal,
                        cultura.getLucroTotal() * qtdFinal
                ));

                ouroRestante -= (qtdFinal * custoUnidade);
                soloRestante -= qtdFinal;
                lucroTotalPlano += (cultura.getLucroTotal() * qtdFinal);
            }
        }

        return PlanoPlantioDTO.builder()
                .lucroTotalEstimado(Math.round(lucroTotalPlano * 100.0) / 100.0)
                .ouroRestante(ouroRestante)
                .espacoRestante(soloRestante)
                .itens(itensSugerido)
                .build();
    }
}