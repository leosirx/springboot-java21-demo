package com.example.demo.domain.port;

import com.example.demo.domain.model.Artist;

import java.util.List;
import java.util.Optional;

public interface ArtistRepositoryPort {
    List<Artist> findAll();
    Optional<Artist> findById(Integer id);
    Artist save(Artist artist);
    void deleteById(Integer id);
}
