package com.stardew.cropplanner.controller;

import com.stardew.cropplanner.dto.CropROIResponseDTO;
import com.stardew.cropplanner.entity.Crop;
import com.stardew.cropplanner.entity.PlayerState;
import com.stardew.cropplanner.repository.CropRepository;
import com.stardew.cropplanner.repository.PlayerStateRepository;
import com.stardew.cropplanner.service.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/optimize")
public class OptimizationController {

    @Autowired
    private ProfitService profitService;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private PlayerStateRepository playerStateRepository;

    @GetMapping("/best-crops")
    public List<CropROIResponseDTO> getBestCrops(
            @RequestParam Long playerId,
            @RequestParam Integer seedCost){
        PlayerState player = playerStateRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        List<Crop> crops = cropRepository.findBySeason((player.getCurrentSeason()));
        int daysRemaining = 28 - player.getCurrentDay();

        return crops.stream()
                .map(crop -> profitService.calculateROI(crop, player, seedCost, daysRemaining))
                .collect(Collectors.toList());
    }
}
