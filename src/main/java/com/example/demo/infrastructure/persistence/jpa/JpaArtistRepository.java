package com.example.demo.infrastructure.persistence.jpa;

import com.example.demo.infrastructure.persistence.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaArtistRepository extends JpaRepository<ArtistEntity, Integer> {
}
