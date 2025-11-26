package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Artist") // coincide con el nombre de la tabla en SQLite
public class Artist {

    @Id
    @Column(name = "ArtistId")
    private Integer artistId;

    @Column(name = "Name")
    private String name;

    /*// Getters y setters
    public Integer getArtistId() { return ArtistId; }
    public void setArtistId(Integer artistId) { ArtistId = artistId; }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }*/
}

