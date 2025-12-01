package com.example.demo.repository;

import com.example.demo.artists.model.Artist;
import com.example.demo.artists.repository.ArtistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ArtistRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArtistRepository artistRepository;

    // Tests para findAll (ya los tienes)
    @Test
    @DisplayName("Given artists in database, when finding all, then return all artists")
    void givenArtistsInDatabase_whenFindAll_thenReturnAllArtists() {
        // Given
        Artist artist1 = new Artist(null, "The Beatles");
        Artist artist2 = new Artist(null, "Queen");

        artistRepository.saveAll(List.of(artist1, artist2));

        // When
        List<Artist> artists = artistRepository.findAll();

        // Then
        assertThat(artists).hasSize(2);
        assertThat(artists).extracting(Artist::getName)
                .containsExactlyInAnyOrder("The Beatles", "Queen");
    }

    @Test
    @DisplayName("Given empty database, when finding all, then return empty list")
    void givenEmptyDatabase_whenFindAll_thenReturnEmptyList() {
        // When
        List<Artist> artists = artistRepository.findAll();

        // Then
        assertThat(artists).isEmpty();
    }

    // Tests para findById
    @Test
    @DisplayName("Given existing artist ID, when finding by ID, then return artist")
    void givenExistingArtistId_whenFindById_thenReturnArtist() {
        // Given
        Artist artist = new Artist(null, "The Beatles");
        Artist savedArtist = artistRepository.save(artist);

        // When
        Optional<Artist> foundArtist = artistRepository.findById(savedArtist.getArtistId());

        // Then
        assertThat(foundArtist).isPresent();
        assertThat(foundArtist.get().getArtistId()).isEqualTo(savedArtist.getArtistId());
        assertThat(foundArtist.get().getName()).isEqualTo("The Beatles");
    }

    @Test
    @DisplayName("Given non-existent artist ID, when finding by ID, then return empty optional")
    void givenNonExistentArtistId_whenFindById_thenReturnEmptyOptional() {
        // When
        Optional<Artist> foundArtist = artistRepository.findById(999);

        // Then
        assertThat(foundArtist).isEmpty();
    }

    // Tests para save
    @Test
    @DisplayName("Given new artist, when saving, then persist with generated ID")
    void givenNewArtist_whenSaving_thenPersistWithGeneratedId() {
        // Given
        Artist newArtist = new Artist(null, "New Artist");

        // When
        Artist savedArtist = artistRepository.save(newArtist);

        // Then
        assertThat(savedArtist.getArtistId()).isNotNull();
        assertThat(savedArtist.getName()).isEqualTo("New Artist");

        // Verificar que realmente se persistió
        Artist foundArtist = artistRepository.findById(savedArtist.getArtistId()).orElseThrow();
        assertThat(foundArtist.getName()).isEqualTo("New Artist");
    }

    @Test
    @DisplayName("Given existing artist, when updating, then update data")
    void givenExistingArtist_whenUpdating_thenUpdateData() {
        // Given
        Artist artist = new Artist(null, "Old Name");
        Artist savedArtist = artistRepository.save(artist);

        // When - Actualizar el artista
        savedArtist.setName("Updated Name");
        Artist updatedArtist = artistRepository.save(savedArtist);

        // Then
        assertThat(updatedArtist.getArtistId()).isEqualTo(savedArtist.getArtistId());
        assertThat(updatedArtist.getName()).isEqualTo("Updated Name");

        // Verificar en la base de datos
        Artist foundArtist = artistRepository.findById(savedArtist.getArtistId()).orElseThrow();
        assertThat(foundArtist.getName()).isEqualTo("Updated Name");
    }

    // Tests para deleteById
    @Test
    @DisplayName("Given existing artist, when deleting by ID, then remove from database")
    void givenExistingArtist_whenDeletingById_thenRemoveFromDatabase() {
        // Given
        Artist artist = new Artist(null, "Artist to Delete");
        Artist savedArtist = artistRepository.save(artist);
        Integer artistId = savedArtist.getArtistId();

        // Verificar que existe antes de eliminar
        assertThat(artistRepository.findById(artistId)).isPresent();

        // When
        artistRepository.deleteById(artistId);

        // Then
        assertThat(artistRepository.findById(artistId)).isEmpty();
    }

    @Test
    @DisplayName("Given non-existent artist ID, when deleting by ID, then do nothing")
    void givenNonExistentArtistId_whenDeletingById_thenDoNothing() {
        // Given
        Integer nonExistentId = 999;

        // Verificar que no existe
        assertThat(artistRepository.findById(nonExistentId)).isEmpty();

        // When - No debería lanzar excepción
        artistRepository.deleteById(nonExistentId);

        // Then - La base de datos sigue igual
        assertThat(artistRepository.findById(nonExistentId)).isEmpty();
    }

    // Tests para saveAll
    @Test
    @DisplayName("Given multiple artists, when saving all, then persist all artists")
    void givenMultipleArtists_whenSavingAll_thenPersistAllArtists() {
        // Given
        Artist artist1 = new Artist(null, "Artist 1");
        Artist artist2 = new Artist(null, "Artist 2");
        Artist artist3 = new Artist(null, "Artist 3");
        List<Artist> artists = List.of(artist1, artist2, artist3);

        // When
        List<Artist> savedArtists = artistRepository.saveAll(artists);

        // Then
        assertThat(savedArtists).hasSize(3);
        assertThat(savedArtists).allMatch(artist -> artist.getArtistId() != null);
        assertThat(savedArtists).extracting(Artist::getName)
                .containsExactly("Artist 1", "Artist 2", "Artist 3");

        // Verificar en la base de datos
        List<Artist> allArtists = artistRepository.findAll();
        assertThat(allArtists).hasSize(3);
    }

    // Tests adicionales para operaciones en lote
    @Test
    @DisplayName("Given multiple operations, when performing CRUD operations, then work correctly")
    void givenMultipleOperations_whenPerformingCrudOperations_thenWorkCorrectly() {
        // Given - Crear algunos artistas
        Artist artist1 = artistRepository.save(new Artist(null, "Artist A"));
        Artist artist2 = artistRepository.save(new Artist(null, "Artist B"));

        // When - Realizar múltiples operaciones
        long initialCount = artistRepository.count();

        // Actualizar uno
        artist1.setName("Updated Artist A");
        artistRepository.save(artist1);

        // Eliminar otro
        artistRepository.deleteById(artist2.getArtistId());

        // Crear uno nuevo
        Artist artist3 = artistRepository.save(new Artist(null, "Artist C"));

        // Then - Verificar estado final
        List<Artist> finalArtists = artistRepository.findAll();
        assertThat(finalArtists).hasSize(2); // A actualizado + C nuevo

        Artist foundArtist1 = artistRepository.findById(artist1.getArtistId()).orElseThrow();
        assertThat(foundArtist1.getName()).isEqualTo("Updated Artist A");

        assertThat(artistRepository.findById(artist2.getArtistId())).isEmpty();

        Artist foundArtist3 = artistRepository.findById(artist3.getArtistId()).orElseThrow();
        assertThat(foundArtist3.getName()).isEqualTo("Artist C");
    }

    // Test para verificar que los IDs son únicos
    @Test
    @DisplayName("When saving multiple artists, then each has unique ID")
    void whenSavingMultipleArtists_thenEachHasUniqueId() {
        // Given
        Artist artist1 = new Artist(null, "Artist 1");
        Artist artist2 = new Artist(null, "Artist 2");

        // When
        Artist saved1 = artistRepository.save(artist1);
        Artist saved2 = artistRepository.save(artist2);

        // Then
        assertThat(saved1.getArtistId()).isNotNull();
        assertThat(saved2.getArtistId()).isNotNull();
        assertThat(saved1.getArtistId()).isNotEqualTo(saved2.getArtistId());
    }

    // Test para count
    @Test
    @DisplayName("Given artists in database, when counting, then return correct count")
    void givenArtistsInDatabase_whenCounting_thenReturnCorrectCount() {
        // Given
        artistRepository.save(new Artist(null, "Artist 1"));
        artistRepository.save(new Artist(null, "Artist 2"));
        artistRepository.save(new Artist(null, "Artist 3"));

        // When
        long count = artistRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Given empty database, when counting, then return zero")
    void givenEmptyDatabase_whenCounting_thenReturnZero() {
        // When
        long count = artistRepository.count();

        // Then
        assertThat(count).isZero();
    }
}
