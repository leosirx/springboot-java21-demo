package com.example.demo.application.service;

import com.example.demo.application.port.ArtistServicePort;
import com.example.demo.domain.model.Artist;
import com.example.demo.domain.port.ArtistRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService implements ArtistServicePort {

    private final   ArtistRepositoryPort artistRepositoryPort;

    @Override
    public List<Artist> getAllArtists() {
        return artistRepositoryPort.findAll();
    }

    @Override
    public Artist getArtistById(Integer id) {
        return artistRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));
    }

    @Override
    public Artist createArtist(Artist artist) {
        return artistRepositoryPort.save(artist);
    }

    @Override
    public void deleteArtist(Integer id) {
        artistRepositoryPort.deleteById(id);
    }
}
