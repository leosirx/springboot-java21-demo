package com.example.demo.artists.repository;

import com.example.demo.artists.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    // Los parámetros genéricos son: <Entidad, Tipo del ID>
}
