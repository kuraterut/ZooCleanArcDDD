package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalNotFoundException;
import org.kuraterut.zoohm2hse.application.services.AnimalService;
import org.kuraterut.zoohm2hse.application.services.EnclosureService;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.events.AnimalFedEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalHealEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalSickEvent;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.*;
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

        Animal result = animalService.createAnimal(request);

        assertNotNull(result);
        assertEquals("Симба", result.getName().getValue());
        assertEquals(AnimalType.PREDATOR, result.getType());
        verify(enclosureService).getEnclosureById(1L);
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void getAnimalById_ExistingId_ReturnsAnimal() {
        Long animalId = 1L;
        Animal expectedAnimal = new Animal();
        expectedAnimal.setId(animalId);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(expectedAnimal));

        Animal result = animalService.getAnimalById(animalId);

        assertEquals(animalId, result.getId());
    }

    @Test
    void getAnimalById_NonExistingId_ThrowsException() {
        Long animalId = 999L;
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        assertThrows(AnimalNotFoundException.class, () -> animalService.getAnimalById(animalId));
    }

    @Test
    @Transactional
    void feedAnimal_ValidId_PublishesEvent() {
        Long animalId = 1L;
        Animal animal = new Animal();
        animal.setId(animalId);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        animalService.feedAnimal(animalId);

        verify(eventPublisher).publishEvent(any(AnimalFedEvent.class));
        verify(animalRepository).save(animal);
    }

    @Test
    void healAnimal_SickAnimal_ChangesHealthStatus() {
        Long animalId = 1L;
        Animal animal = Animal.builder()
                .healthy(new AnimalHealthFlag(false))
                .build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        animalService.healAnimal(animalId);

        assertTrue(animal.getHealthy().isValue());
        verify(eventPublisher).publishEvent(any(AnimalHealEvent.class));
    }

    @Test
    void sickAnimal_HealthyAnimal_ChangesHealthStatus() {
        Long animalId = 1L;
        Animal animal = Animal.builder()
                .healthy(new AnimalHealthFlag(true))
                .build();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));

        animalService.sickAnimal(animalId);

        assertFalse(animal.getHealthy().isValue());
        verify(eventPublisher).publishEvent(any(AnimalSickEvent.class));
    }

    @Test
    void getSickAnimals_ReturnsOnlySickAnimals() {
        Animal sickAnimal = Animal.builder()
                .healthy(new AnimalHealthFlag(false))
                .build();

        List<Animal> sickAnimals = List.of(sickAnimal);

        when(animalRepository.findByHealthyFalse()).thenReturn(sickAnimals);

        List<Animal> result = animalService.getSickAnimals();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getHealthy().isValue());
    }

    @Test
    void deleteAnimal_ExistingId_DeletesAnimal() {
        Long animalId = 1L;
        when(animalRepository.existsById(animalId)).thenReturn(true);

        animalService.deleteAnimal(animalId);

        verify(animalRepository).deleteById(animalId);
    }

    @Test
    void deleteAnimal_NonExistingId_ThrowsException() {
        Long animalId = 999L;
        when(animalRepository.existsById(animalId)).thenReturn(false);

        assertThrows(AnimalNotFoundException.class, () -> animalService.deleteAnimal(animalId));
        verify(animalRepository, never()).deleteById(any());
    }
}