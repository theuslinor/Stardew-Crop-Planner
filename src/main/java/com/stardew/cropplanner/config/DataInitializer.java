package com.stardew.cropplanner.config;

import com.stardew.cropplanner.repository.CulturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CulturaRepository culturaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {

    }
}
