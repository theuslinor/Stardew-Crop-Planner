package com.stardew.cropplanner.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.repository.CulturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CulturaRepository culturaRepository;

    @Autowired
    private ObjectMapper objectMapper; // O Spring agora vai pegar o Bean da JacksonConfig

    @Override
    public void run(String... args) throws Exception {
        if (culturaRepository.count() == 0) {
            System.out.println(">>> [DEBUG] Banco vazio. Iniciando importação SB4...");

            try (InputStream inputStream = getClass().getResourceAsStream("/dados/culturas.json")) {
                if (inputStream == null) {
                    System.out.println(">>> [ERRO] culturas.json não encontrado em resources/dados/");
                    return;
                }

                List<Cultura> culturas = objectMapper.readValue(inputStream, new TypeReference<List<Cultura>>() {});

                culturas.forEach(c -> {
                    if (c.getFontesPreco() != null) {
                        c.getFontesPreco().forEach(f -> f.setCultura(c));
                    }
                });

                culturaRepository.saveAll(culturas);
                System.out.println(">>> [SUCESSO] " + culturas.size() + " culturas importadas via JSON.");
            } catch (Exception e) {
                System.out.println(">>> [ERRO] Falha na importação: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}