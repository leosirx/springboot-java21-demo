package com.example.demo.artists.controller;


import com.example.demo.artists.model.Artist;
import com.example.demo.artists.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.findAllArtist();
    }

    // Endpoints adicionales
    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable Integer id) {
        return artistService.findArtistById(id);
    }

    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.saveArtist(artist);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Integer id) {
        artistService.deleteArtist(id);
    }
}

