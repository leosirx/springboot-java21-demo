package com.example.demo.config;

import com.example.demo.application.port.ArtistServicePort;
import com.example.demo.application.service.ArtistService;
import com.example.demo.domain.port.ArtistRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ArtistServicePort artistServicePort(ArtistRepositoryPort artistRepositoryPort) {
        return new ArtistService(artistRepositoryPort);
    }
}
