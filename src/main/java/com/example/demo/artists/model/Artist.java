package com.example.demo.artists.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Artist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArtistId")
    @Schema(description = "Unique identifier of the artist", example = "1")
    private Integer artistId;

    @Column(name = "Name")
    @Schema(description = "Name of the artist", example = "The Beatles")
    private String name;
}

