package com.example.demo.artists.service;

import com.example.demo.artists.model.Artist;
import com.example.demo.artists.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository repository;

    public List<Artist> findAllArtist() {
        return repository.findAll();
    }

    // Métodos adicionales útiles
    public Artist saveArtist(Artist artist) {
        return repository.save(artist);
    }

    public Artist findArtistById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteArtist(Integer id) {
        repository.deleteById(id);
    }
}
