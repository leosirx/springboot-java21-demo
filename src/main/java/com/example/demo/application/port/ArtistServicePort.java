package com.example.demo.application.port;

import com.example.demo.domain.model.Artist;

import java.util.List;

public interface ArtistServicePort {
    List<Artist> getAllArtists();
    Artist getArtistById(Integer id);
    Artist createArtist(Artist artist);
    void deleteArtist(Integer id);
}
