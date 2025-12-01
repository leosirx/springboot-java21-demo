package com.example.demo.controller;

import com.example.demo.artists.controller.ArtistController;
import com.example.demo.artists.model.Artist;
import com.example.demo.artists.service.ArtistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(controllers = ArtistController.class)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Given artists exist, when getting all artists, then return 200 OK with artist list")
    void givenArtistsExist_whenGetAllArtists_thenReturnOkWithArtistList() throws Exception {
        // Given
        Artist artist1 = new Artist(1, "The Beatles");
        Artist artist2 = new Artist(2, "Queen");
        List<Artist> expectedArtists = Arrays.asList(artist1, artist2);

        when(artistService.findAllArtist()).thenReturn(expectedArtists);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artistId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("The Beatles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artistId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Queen"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedArtists)));
    }

    @Test
    @DisplayName("Given no artists exist, when getting all artists, then return 200 OK with empty list")
    void givenNoArtistsExist_whenGetAllArtists_thenReturnOkWithEmptyList() throws Exception {
        // Given
        when(artistService.findAllArtist()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Given service throws exception, when getting all artists, then return 500 Internal Server Error")
    void givenServiceThrowsException_whenGetAllArtists_thenReturnInternalServerError() throws Exception {
        // Given
        when(artistService.findAllArtist()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }



    @Test
    @DisplayName("Given valid artist data, when creating artist, then return 200 OK with created artist")
    void givenValidArtistData_whenCreateArtist_thenReturnOkWithArtist() throws Exception {
        // Given
        Artist newArtist = new Artist(null, "New Artist");
        Artist savedArtist = new Artist(1, "New Artist");

        when(artistService.saveArtist(any(Artist.class))).thenReturn(savedArtist);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArtist)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Artist"));
    }

    @Test
    @DisplayName("Given invalid artist data, when creating artist, then return 400 Bad Request")
    void givenInvalidArtistData_whenCreateArtist_thenReturnBadRequest() throws Exception {
        // Given - Artist sin nombre (asumiendo que name es requerido)
        Artist invalidArtist = new Artist(null, null);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidArtist)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); // Si tienes validaciones
    }


    @Test
    @DisplayName("Given valid artist ID, when deleting artist, then return 200 OK")
    void givenValidArtistId_whenDeleteArtist_thenReturnOk() throws Exception {
        // Given
        Integer artistId = 1;
        doNothing().when(artistService).deleteArtist(artistId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/artists/{id}", artistId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(artistService, times(1)).deleteArtist(artistId);
    }

    @Test
    @DisplayName("Given non-existent artist ID, when deleting artist, then return 200 OK")
    void givenNonExistentArtistId_whenDeleteArtist_thenReturnOk() throws Exception {
        // Given
        Integer artistId = 999;
        doNothing().when(artistService).deleteArtist(artistId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/artists/{id}", artistId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    @DisplayName("Given valid artist data, when updating artist, then return 200 OK with updated artist")
    void givenValidArtistData_whenUpdateArtist_thenReturnOkWithUpdatedArtist() throws Exception {
        // Given
        Integer artistId = 1;
        Artist updateData = new Artist(null, "Updated Artist Name");
        Artist updatedArtist = new Artist(artistId, "Updated Artist Name");

        when(artistService.updateArtist(eq(artistId), any(Artist.class))).thenReturn(updatedArtist);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistId").value(artistId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Artist Name"));
    }

    @Test
    @DisplayName("Given non-existent artist ID, when updating artist, then return 404 Not Found")
    void givenNonExistentArtistId_whenUpdateArtist_thenReturnNotFound() throws Exception {
        // Given
        Integer artistId = 999;
        Artist updateData = new Artist(null, "Non-existent Artist");

        when(artistService.updateArtist(eq(artistId), any(Artist.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}



