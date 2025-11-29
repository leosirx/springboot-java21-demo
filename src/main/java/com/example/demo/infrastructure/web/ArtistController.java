package com.example.demo.infrastructure.web;


import com.example.demo.application.port.ArtistServicePort;
import com.example.demo.domain.model.Artist;
import com.example.demo.application.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistServicePort artistServicePort;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistServicePort.getAllArtists();
    }

    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable Integer id) {
        return artistServicePort.getArtistById(id);
    }

    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistServicePort.createArtist(artist);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Integer id) {
        artistServicePort.deleteArtist(id);
    }
}

