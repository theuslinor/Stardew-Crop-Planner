package com.stardew.cropplanner.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // Criamos o mapper básico.
        // Ele já é capaz de ler strings, números e booleanos do seu culturas.json
        return new ObjectMapper();
    }
}