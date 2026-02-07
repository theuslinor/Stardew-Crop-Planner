package com.stardew.cropplanner.service;

import com.stardew.cropplanner.entity.Cultura;
import com.stardew.cropplanner.repository.CulturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServicoDataInjection implements CommandLineRunner {

    private final CulturaRepository culturaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception{
        //  RF11 - Só importa se o banco estiver vazio para evitar duplicates
        if(culturaRepository.count() == 0){{
            System.out.println("Iniciando Ingestão de Dados (JSON)...");

            // 1. Localiza o arquivo na pasta resources
            InputStream inputStream = getClass().getResourceAsStream("/dados/culturas.json");

            // 2. Transforma o JSON em uma lista de Objetos Cultura
            List<Cultura> culturas = objectMapper.readValue(inputStream, new TypeReference<List<Cultura>>() {});

            // 3. Configura o relacionamento bidirecional(cultura <-> FonteSemente)
            // Isso é necessário porque o JPA precisa sabe quem é o "pai" de cada fonte
            culturas.forEach(cultura ->{
                if (cultura.getFontesPreco() != null) {
                    cultura.getFontesPreco().forEach(fonte -> fonte.setCultura(cultura));
                }
            });

            // 4. Salva tudo de uma vez no banco
            culturaRepository.saveAll(culturas);

            System.out.println("Sucesso" + culturas.size() + " culturas importadas");
        }}
    }
}
