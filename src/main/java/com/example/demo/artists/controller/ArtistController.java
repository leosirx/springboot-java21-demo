package com.example.demo.artists.controller;


import com.example.demo.artists.model.Artist;
import com.example.demo.artists.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
@Tag(name = "Artists", description = "API for Artists management")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    @Operation(summary = "Get all artists", description = "Retrieve a list of all artists")
    public List<Artist> getAllArtists() {
        return artistService.findAllArtist();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get artist by ID", description = "Retrieve a specific artist by its ID")
    public Artist getArtistById(
            @Parameter(description = "ID of the artist to retrieve", example = "1")
            @PathVariable Integer id) {
        return artistService.findArtistById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new artist", description = "Create a new artist in the system")
    public Artist createArtist(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Artist object to create")
            @RequestBody Artist artist) {
        return artistService.saveArtist(artist);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an artist", description = "Delete a specific artist by its ID")
    public void deleteArtist(
            @Parameter(description = "ID of the artist to delete", example = "1")
            @PathVariable Integer id) {
        artistService.deleteArtist(id);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update entire artist", description = "Replace all data of an existing artist")
    public ResponseEntity<Artist> updateArtist(
            @Parameter(description = "ID of the artist to update", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated artist data")
            @RequestBody Artist artist) {

        Artist updatedArtist = artistService.updateArtist(id, artist);
        if (updatedArtist != null) {
            return ResponseEntity.ok(updatedArtist);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}

