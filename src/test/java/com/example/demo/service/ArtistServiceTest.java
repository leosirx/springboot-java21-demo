package com.example.demo.service;

import com.example.demo.artists.model.Artist;
import com.example.demo.artists.repository.ArtistRepository;
import com.example.demo.artists.service.ArtistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    // Tests para findAllArtist (ya los tienes)
    @Test
    @DisplayName("Given artists exist, when finding all artists, then return artist list")
    void givenArtistsExist_whenFindAllArtists_thenReturnArtistList() {
        // Given
        Artist artist1 = new Artist(1, "The Beatles");
        Artist artist2 = new Artist(2, "Queen");
        List<Artist> expectedArtists = Arrays.asList(artist1, artist2);

        when(artistRepository.findAll()).thenReturn(expectedArtists);

        // When
        List<Artist> actualArtists = artistService.findAllArtist();

        // Then
        assertThat(actualArtists).hasSize(2);
        assertThat(actualArtists).containsExactly(artist1, artist2);
        verify(artistRepository, times(1)).findAll();
        verifyNoMoreInteractions(artistRepository);
    }

    @Test
    @DisplayName("Given no artists exist, when finding all artists, then return empty list")
    void givenNoArtistsExist_whenFindAllArtists_thenReturnEmptyList() {
        // Given
        when(artistRepository.findAll()).thenReturn(List.of());

        // When
        List<Artist> actualArtists = artistService.findAllArtist();

        // Then
        assertThat(actualArtists).isEmpty();
        verify(artistRepository, times(1)).findAll();
    }

    // Tests para findArtistById
    @Test
    @DisplayName("Given existing artist ID, when finding artist by ID, then return artist")
    void givenExistingArtistId_whenFindArtistById_thenReturnArtist() {
        // Given
        Integer artistId = 1;
        Artist expectedArtist = new Artist(artistId, "The Beatles");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(expectedArtist));

        // When
        Artist actualArtist = artistService.findArtistById(artistId);

        // Then
        assertThat(actualArtist).isEqualTo(expectedArtist);
        verify(artistRepository, times(1)).findById(artistId);
    }

    @Test
    @DisplayName("Given non-existent artist ID, when finding artist by ID, then return null")
    void givenNonExistentArtistId_whenFindArtistById_thenReturnNull() {
        // Given
        Integer artistId = 999;

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        // When
        Artist actualArtist = artistService.findArtistById(artistId);

        // Then
        assertThat(actualArtist).isNull();
        verify(artistRepository, times(1)).findById(artistId);
    }

    // Tests para saveArtist
    @Test
    @DisplayName("Given valid artist, when saving artist, then return saved artist")
    void givenValidArtist_whenSaveArtist_thenReturnSavedArtist() {
        // Given
        Artist artistToSave = new Artist(null, "New Artist");
        Artist savedArtist = new Artist(1, "New Artist");

        when(artistRepository.save(artistToSave)).thenReturn(savedArtist);

        // When
        Artist result = artistService.saveArtist(artistToSave);

        // Then
        assertThat(result).isEqualTo(savedArtist);
        verify(artistRepository, times(1)).save(artistToSave);
    }

    @Test
    @DisplayName("Given repository throws exception, when saving artist, then propagate exception")
    void givenRepositoryThrowsException_whenSaveArtist_thenPropagateException() {
        // Given
        Artist artistToSave = new Artist(null, "New Artist");

        when(artistRepository.save(artistToSave)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> artistService.saveArtist(artistToSave))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
        verify(artistRepository, times(1)).save(artistToSave);
    }

    // Tests para deleteArtist
    @Test
    @DisplayName("Given existing artist ID, when deleting artist, then delete successfully")
    void givenExistingArtistId_whenDeleteArtist_thenDeleteSuccessfully() {
        // Given
        Integer artistId = 1;
        doNothing().when(artistRepository).deleteById(artistId);

        // When
        artistService.deleteArtist(artistId);

        // Then
        verify(artistRepository, times(1)).deleteById(artistId);
    }

    @Test
    @DisplayName("Given non-existent artist ID, when deleting artist, then do nothing")
    void givenNonExistentArtistId_whenDeleteArtist_thenDoNothing() {
        // Given
        Integer artistId = 999;
        doNothing().when(artistRepository).deleteById(artistId);

        // When
        artistService.deleteArtist(artistId);

        // Then
        verify(artistRepository, times(1)).deleteById(artistId);
    }

    @Test
    @DisplayName("Given repository throws exception, when deleting artist, then propagate exception")
    void givenRepositoryThrowsException_whenDeleteArtist_thenPropagateException() {
        // Given
        Integer artistId = 1;
        doThrow(new RuntimeException("Database error")).when(artistRepository).deleteById(artistId);

        // When & Then
        assertThatThrownBy(() -> artistService.deleteArtist(artistId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
        verify(artistRepository, times(1)).deleteById(artistId);
    }

    // Tests para updateArtist
    @Test
    @DisplayName("Given existing artist ID and valid data, when updating artist, then return updated artist")
    void givenExistingArtistIdAndValidData_whenUpdateArtist_thenReturnUpdatedArtist() {
        // Given
        Integer artistId = 1;
        Artist existingArtist = new Artist(artistId, "Old Name");
        Artist updateData = new Artist(null, "New Name");
        Artist updatedArtist = new Artist(artistId, "New Name");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(updatedArtist);

        // When
        Artist result = artistService.updateArtist(artistId, updateData);

        // Then
        assertThat(result).isEqualTo(updatedArtist);
        assertThat(result.getName()).isEqualTo("New Name");
        verify(artistRepository, times(1)).findById(artistId);
        verify(artistRepository, times(1)).save(any(Artist.class));
    }

    @Test
    @DisplayName("Given non-existent artist ID, when updating artist, then throw exception")
    void givenNonExistentArtistId_whenUpdateArtist_thenThrowException() {
        // Given
        Integer artistId = 999;
        Artist updateData = new Artist(null, "New Name");

        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> artistService.updateArtist(artistId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Artist not found with id: " + artistId);
        verify(artistRepository, times(1)).findById(artistId);
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    @DisplayName("Given existing artist ID, when updating artist and save fails, then propagate exception")
    void givenExistingArtistId_whenUpdateArtistAndSaveFails_thenPropagateException() {
        // Given
        Integer artistId = 1;
        Artist existingArtist = new Artist(artistId, "Old Name");
        Artist updateData = new Artist(null, "New Name");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenThrow(new RuntimeException("Save failed"));

        // When & Then
        assertThatThrownBy(() -> artistService.updateArtist(artistId, updateData))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Save failed");
        verify(artistRepository, times(1)).findById(artistId);
        verify(artistRepository, times(1)).save(any(Artist.class));
    }

    // Test adicional para verificar que se setea el ID correctamente en update
    @Test
    @DisplayName("Given existing artist ID, when updating artist, then set correct ID on artist to save")
    void givenExistingArtistId_whenUpdateArtist_thenSetCorrectId() {
        // Given
        Integer artistId = 1;
        Artist existingArtist = new Artist(artistId, "Old Name");
        Artist updateData = new Artist(null, "New Name");

        when(artistRepository.findById(artistId)).thenReturn(Optional.of(existingArtist));
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> {
            Artist artistToSave = invocation.getArgument(0);
            assertThat(artistToSave.getArtistId()).isEqualTo(artistId); // Verificar que el ID se setea correctamente
            return artistToSave;
        });

        // When
        artistService.updateArtist(artistId, updateData);

        // Then - La verificación está en el thenAnswer
        verify(artistRepository, times(1)).findById(artistId);
        verify(artistRepository, times(1)).save(any(Artist.class));
    }
}
