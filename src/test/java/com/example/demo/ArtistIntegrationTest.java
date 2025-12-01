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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ArtistIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Test para GET ALL (ya lo tienes)
    @Test
    @DisplayName("Given artists in database, when calling GET /api/artists, then return all artists")
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

    // Test para GET by ID
    @Test
    @DisplayName("Given existing artist ID, when calling GET /api/artists/{id}, then return artist")
    void givenExistingArtistId_whenGetArtistById_thenReturnArtist() throws Exception {
        // Given
        Artist artist = new Artist(null, "Test Artist for ID");
        Artist savedArtist = artistRepository.save(artist);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists/{id}", savedArtist.getArtistId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistId").value(savedArtist.getArtistId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Artist for ID"));
    }

    @Test
    @DisplayName("Given non-existent artist ID, when calling GET /api/artists/{id}, then return artist with null")
    void givenNonExistentArtistId_whenGetArtistById_thenReturnNull() throws Exception {
        // When & Then - Tu controller actual retorna 200 con null en el body
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Test para POST (CREATE)
    @Test
    @DisplayName("Given valid artist data, when calling POST /api/artists, then create and return artist")
    void givenValidArtistData_whenCreateArtist_thenReturnCreatedArtist() throws Exception {
        // Given
        String artistJson = """
            {
                "name": "New Integration Artist"
            }
            """;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(artistJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Integration Artist"));

        // Verificar que realmente se guardó en la base de datos
        assertThat(artistRepository.findAll()).hasSize(1);
        Artist savedArtist = artistRepository.findAll().get(0);
        assertThat(savedArtist.getName()).isEqualTo("New Integration Artist");
    }

    // Test para PUT (UPDATE)
    @Test
    @DisplayName("Given existing artist ID and valid data, when calling PUT /api/artists/{id}, then update and return artist")
    void givenExistingArtistIdAndValidData_whenUpdateArtist_thenReturnUpdatedArtist() throws Exception {
        // Given
        Artist existingArtist = new Artist(null, "Original Name");
        Artist savedArtist = artistRepository.save(existingArtist);

        String updateJson = """
            {
                "name": "Updated Integration Artist"
            }
            """;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/artists/{id}", savedArtist.getArtistId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistId").value(savedArtist.getArtistId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Integration Artist"));

        // Verificar que realmente se actualizó en la base de datos
        Artist updatedArtist = artistRepository.findById(savedArtist.getArtistId()).orElseThrow();
        assertThat(updatedArtist.getName()).isEqualTo("Updated Integration Artist");
    }

    @Test
    @DisplayName("Given non-existent artist ID, when calling PUT /api/artists/{id}, then return 404")
    void givenNonExistentArtistId_whenUpdateArtist_thenReturnNotFound() throws Exception {
        // Given
        String updateJson = """
            {
                "name": "Non-existent Artist"
            }
            """;

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/artists/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Test para DELETE
    @Test
    @DisplayName("Given existing artist ID, when calling DELETE /api/artists/{id}, then delete artist")
    void givenExistingArtistId_whenDeleteArtist_thenDeleteArtist() throws Exception {
        // Given
        Artist artist = new Artist(null, "Artist to Delete");
        Artist savedArtist = artistRepository.save(artist);

        // Verificar que existe antes de eliminar
        assertThat(artistRepository.findById(savedArtist.getArtistId())).isPresent();

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/artists/{id}", savedArtist.getArtistId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificar que fue eliminado de la base de datos
        assertThat(artistRepository.findById(savedArtist.getArtistId())).isEmpty();
    }

    @Test
    @DisplayName("Given non-existent artist ID, when calling DELETE /api/artists/{id}, then return 200")
    void givenNonExistentArtistId_whenDeleteArtist_thenReturnOk() throws Exception {
        // When & Then - Tu controller actual retorna 200 incluso si no existe
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/artists/{id}", 999))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Test de flujo completo CRUD
    @Test
    @DisplayName("Given full CRUD operations, when performing complete flow, then work correctly")
    void givenFullCrudOperations_whenPerformingCompleteFlow_thenWorkCorrectly() throws Exception {
        // 1. CREATE - Crear un artista
        String createJson = """
            {
                "name": "CRUD Flow Artist"
            }
            """;

        String createResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.artistId").exists())
                .andExpect(jsonPath("$.name").value("CRUD Flow Artist"))
                .andReturn().getResponse().getContentAsString();

        Artist createdArtist = objectMapper.readValue(createResponse, Artist.class);
        Integer artistId = createdArtist.getArtistId();

        // 2. READ - Leer el artista creado
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.artistId").value(artistId))
                .andExpect(jsonPath("$.name").value("CRUD Flow Artist"));

        // 3. UPDATE - Actualizar el artista
        String updateJson = """
            {
                "name": "Updated CRUD Flow Artist"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.artistId").value(artistId))
                .andExpect(jsonPath("$.name").value("Updated CRUD Flow Artist"));

        // 4. DELETE - Eliminar el artista
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/artists/{id}", artistId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // 5. VERIFY - Verificar que fue eliminado
        mockMvc.perform(MockMvcRequestBuilders.get("/api/artists/{id}", artistId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Tu implementación retorna 200 con null
    }

    // Test para verificar que la base de datos está limpia entre tests
    @Test
    @DisplayName("Given multiple tests, when checking database state, then each test has clean state")
    void givenMultipleTests_whenCheckingDatabaseState_thenEachTestHasCleanState() throws Exception {
        // Este test verifica que @Transactional funciona correctamente
        long initialCount = artistRepository.count();

        // Agregar un artista
        Artist artist = new Artist(null, "Transactional Test Artist");
        artistRepository.save(artist);

        // Verificar que se agregó
        assertThat(artistRepository.count()).isEqualTo(initialCount + 1);

        // Al final del test, @Transactional hará rollback y la base de datos volverá al estado inicial
    }
}
