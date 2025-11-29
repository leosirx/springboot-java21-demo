package com.example.demo.infrastructure.persistence;

import com.example.demo.domain.model.Artist;
import com.example.demo.domain.port.ArtistRepositoryPort;
import com.example.demo.infrastructure.persistence.entity.ArtistEntity;
import com.example.demo.infrastructure.persistence.jpa.JpaArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ArtistRepositoryAdapter implements ArtistRepositoryPort {

    private final JpaArtistRepository jpaArtistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public List<Artist> findAll() {
        return jpaArtistRepository.findAll()
                .stream()
                .map(artistMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Artist> findById(Integer id) {
        return jpaArtistRepository.findById(id)
                .map(artistMapper::toDomain);
    }

    @Override
    public Artist save(Artist artist) {
        ArtistEntity entity = artistMapper.toEntity(artist);
        ArtistEntity savedEntity = jpaArtistRepository.save(entity);
        return artistMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Integer id) {
        jpaArtistRepository.deleteById(id);
    }
}
