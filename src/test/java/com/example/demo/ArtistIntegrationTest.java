package com.example.demo;

import com.example.demo.artists.model.Artist;
import com.example.demo.artists.repository.ArtistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ArtistIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @DisplayName("Given artists in database, when calling GET /artists, then return all artists")
    void givenArtistsInDatabase_whenGetAllArtists_thenReturnArtists() throws Exception {
        // Given
        Artist artist1 = new Artist(null, "Integration Test Artist 1");
        Artist artist2 = new Artist(null, "Integration Test Artist 2");
        artistRepository.save(artist1);
        artistRepository.save(artist2);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").exists());
    }
}
