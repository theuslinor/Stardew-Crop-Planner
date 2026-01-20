package com.stardew.cropplanner.service;

import com.stardew.cropplanner.dto.CropROIResponseDTO;
import com.stardew.cropplanner.entity.Crop;
import com.stardew.cropplanner.entity.PlayerState;
import com.stardew.cropplanner.enums.Profession;
import org.springframework.stereotype.Service;

@Service
public class ProfitService {

    public CropROIResponseDTO calculateROI(Crop crop, PlayerState player, Integer seedCost, Integer daysRemaining) {
        // 1. Calcular quantas colheitas são possiveis no tempo restante
        int harvests = 0;
        if (daysRemaining >= crop.getGrowthTime()) {
            if (crop.isRecurring()) {
                int remainingDaysAfterFirst = daysRemaining - crop.getGrowthTime();
                harvests += (remainingDaysAfterFirst / crop.getGrowthTime());
            }
        }
        // 2. Aplicar o bonus de profissão (Tiller dá 10% a mais)
        double sellPrice = crop.getPriceNormal();
        if (player.getProfession() == Profession.TILLER){
            sellPrice = sellPrice * 1.1;
        }

        // 3. Calculos Financeiros
        double totalRevenue = harvests * sellPrice;
        double totalProfit = totalRevenue - seedCost;
        double roi = (seedCost > 0) ? (totalProfit / seedCost) * 100 : 0;

        return CropROIResponseDTO.builder()
                .cropName(crop.getName())
                .totalProfit(totalProfit)
                .roiPercentage(roi)
                .totalHarvest(harvests)
                .message(harvests == 0 ? "Não há tempo hábil para colher!" : "Cálculo otimizado")
                .build();
    }

}
