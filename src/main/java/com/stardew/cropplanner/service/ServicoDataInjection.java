package com.stardew.cropplanner.service;

import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.repository.CulturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
public class ServicoDataInjection implements CommandLineRunner {
    @Autowired
    private CulturaRepository culturaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception{
        if (culturaRepository.count() == 0){
            InputStream inputStream = getClass().getResourceAsStream("/dados/culturas.json");
            List<Cultura> culturas = objectMapper.readValue(inputStream, new TypeReference<List<Cultura>>() {
            });

            // Configura o relacionamento  bidirecional para o JPA
            culturas.forEach(c -> {
                if (c.getFontesPreco() != null){
                    c.getFontesPreco().forEach(f -> f.setCultura(c));
                }
            });

            culturaRepository.saveAll(culturas);
            System.out.println("Dados de culturas importados com sucesso via JSON!");
        }
    }
}
