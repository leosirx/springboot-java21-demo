package com.example.demo.infrastructure.persistence;

import com.example.demo.domain.model.Artist;
import com.example.demo.infrastructure.persistence.entity.ArtistEntity;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public Artist toDomain(ArtistEntity entity) {
        return new Artist(entity.getArtistId(), entity.getName());
    }

    public ArtistEntity toEntity(Artist domain) {
        return new ArtistEntity(domain.getArtistId(), domain.getName());
    }
}
