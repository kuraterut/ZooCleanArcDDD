package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalNotFoundException;
import org.kuraterut.zoohm2hse.application.services.AnimalService;
import org.kuraterut.zoohm2hse.application.services.EnclosureService;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.domain.events.AnimalFedEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalHealEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalSickEvent;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.*;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateAnimalRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private EnclosureService enclosureService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AnimalService animalService;

    @Test
    void createAnimal_ValidRequest_ReturnsSavedAnimal() {
        // Arrange
        CreateAnimalRequest request = new CreateAnimalRequest(
                "Симба", AnimalType.PREDATOR, LocalDate.of(2020, 5, 15),
                Gender.MALE, FoodType.MEAT, true, 1L
        );

        Enclosure enclosure = new Enclosure();
        enclosure.setId(1L);

        Animal expectedAnimal = Animal.builder()
                .name(new AnimalName("Симба"))
                .type(AnimalType.PREDATOR)
                .birthDate(new AnimalBirthday(LocalDate.of(2020, 5, 15)))
                .gender(Gender.MALE)
                .favoriteFood(FoodType.MEAT)
                .healthy(new AnimalHealthFlag(true))
                .enclosure(enclosure)
                .build();

        when(enclosureService.getEnclosureById(1L)).thenReturn(enclosure);
        when(animalRepository.save(any(Animal.class))).thenReturn(expectedAnimal);

        // Act
        Animal result = animalService.createAnimal(request);

        // Assert
        assertNotNull(result);
        assertEquals("Симба", result.getName().getValue());
        assertEquals(AnimalType.PREDATOR, result.getType());
        verify(enclosureService).getEnclosureById(1L);
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void getAnimalById_ExistingId_ReturnsAnimal() {
        // Arrange
        Long animalId = 1L;
        Animal expectedAnimal = new Animal();
        expectedAnimal.setId(animalId);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(expectedAnimal));

        // Act
        Animal result = animalService.getAnimalById(animalId);

        // Assert
        assertEquals(animalId, result.getId());
    }

    @Test
    void getAnimalById_NonExistingId_ThrowsException() {
        // Arrange
        Long animalId = 999L;
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AnimalNotFoundException.class, () -> animalService.getAnimalById(animalId));
    }

    @Test
    @Transactional
    void feedAnimal_ValidId_PublishesEvent() {
        // Arrange
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setId(animalId);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        // Act
        animalService.feedAnimal(animalId);

        // Assert
        verify(eventPublisher).publishEvent(any(AnimalFedEvent.class));
        verify(animalRepository).save(animal);
    }

    @Test
    void healAnimal_SickAnimal_ChangesHealthStatus() {
        // Arrange
        Long animalId = 1L;
        Animal animal = Animal.builder()
                .healthy(new AnimalHealthFlag(false))
                .build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        // Act
        animalService.healAnimal(animalId);

        // Assert
        assertTrue(animal.getHealthy().isValue());
        verify(eventPublisher).publishEvent(any(AnimalHealEvent.class));
    }

    @Test
    void sickAnimal_HealthyAnimal_ChangesHealthStatus() {
        // Arrange
        Long animalId = 1L;
        Animal animal = Animal.builder()
                .healthy(new AnimalHealthFlag(true))
                .build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        // Act
        animalService.sickAnimal(animalId);

        // Assert
        assertFalse(animal.getHealthy().isValue());
        verify(eventPublisher).publishEvent(any(AnimalSickEvent.class));
    }

    @Test
    void updateAnimal_ValidData_ReturnsUpdatedAnimal() {
        // Arrange
        Long animalId = 1L;
        Animal existingAnimal = Animal.builder()
                .id(animalId)
                .name(new AnimalName("Старое имя"))
                .build();

        Animal updatedDetails = Animal.builder()
                .name(new AnimalName("Новое имя"))
                .build();

        when(animalRepository.existsById(animalId)).thenReturn(true);
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(any(Animal.class))).thenReturn(existingAnimal);

        // Act
        Animal result = animalService.updateAnimal(animalId, updatedDetails);

        // Assert
        assertEquals("Новое имя", result.getName().getValue());
    }

    @Test
    void getSickAnimals_ReturnsOnlySickAnimals() {
        // Arrange
        Animal sickAnimal = Animal.builder()
                .healthy(new AnimalHealthFlag(false))
                .build();

        List<Animal> sickAnimals = List.of(sickAnimal);

        when(animalRepository.findByHealthyFalse()).thenReturn(sickAnimals);

        // Act
        List<Animal> result = animalService.getSickAnimals();

        // Assert
        assertEquals(1, result.size());
        assertFalse(result.get(0).getHealthy().isValue());
    }

    @Test
    void deleteAnimal_ExistingId_DeletesAnimal() {
        // Arrange
        Long animalId = 1L;
        when(animalRepository.existsById(animalId)).thenReturn(true);

        // Act
        animalService.deleteAnimal(animalId);

        // Assert
        verify(animalRepository).deleteById(animalId);
    }

    @Test
    void deleteAnimal_NonExistingId_ThrowsException() {
        // Arrange
        Long animalId = 999L;
        when(animalRepository.existsById(animalId)).thenReturn(false);

        // Act & Assert
        assertThrows(AnimalNotFoundException.class, () -> animalService.deleteAnimal(animalId));
        verify(animalRepository, never()).deleteById(any());
    }
}