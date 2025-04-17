package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalNotFoundException;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureNotFoundException;
import org.kuraterut.zoohm2hse.application.services.AnimalTransferService;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.events.AnimalMovedEvent;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureMaxCapacity;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureType;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnimalTransferServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private EnclosureRepository enclosureRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AnimalTransferService transferService;

    @Test
    void transferAnimal_ValidTransfer_PublishesEvent() {
        Long animalId = 1L;
        Long targetEnclosureId = 2L;
        Long sourceEnclosureId = 3L;

        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setType(AnimalType.HERBIVORE);

        Enclosure sourceEnclosure = new Enclosure();
        sourceEnclosure.setId(sourceEnclosureId);
        sourceEnclosure.setMaxCapacity(new EnclosureMaxCapacity(10));
        sourceEnclosure.setType(EnclosureType.HERBIVORE_ENCLOSURE);
        sourceEnclosure.addAnimal(animal);

        Enclosure targetEnclosure = new Enclosure();
        targetEnclosure.setId(targetEnclosureId);
        targetEnclosure.setMaxCapacity(new EnclosureMaxCapacity(10));
        targetEnclosure.setType(EnclosureType.HERBIVORE_ENCLOSURE);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(targetEnclosureId)).thenReturn(Optional.of(targetEnclosure));

        transferService.transferAnimal(animalId, targetEnclosureId);

        assertEquals(targetEnclosure, animal.getEnclosure());
        assertFalse(sourceEnclosure.getAnimals().contains(animal));
        assertTrue(targetEnclosure.getAnimals().contains(animal));

        verify(animalRepository).save(animal);
        verify(enclosureRepository).save(targetEnclosure);
        verify(eventPublisher).publishEvent(argThat(event ->
                ((AnimalMovedEvent) event).getAnimalId().equals(animalId) &&
                        ((AnimalMovedEvent) event).getFromEnclosureId().equals(sourceEnclosureId) &&
                        ((AnimalMovedEvent) event).getToEnclosureId().equals(targetEnclosureId)
        ));
    }

    @Test
    void transferAnimal_AnimalNotFound_ThrowsException() {
        Long animalId = 999L;
        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());

        assertThrows(AnimalNotFoundException.class, () ->
                transferService.transferAnimal(animalId, 1L));
    }

    @Test
    void transferAnimal_EnclosureNotFound_ThrowsException() {
        Long animalId = 1L;
        Long invalidEnclosureId = 999L;
        Animal animal = new Animal();

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(invalidEnclosureId)).thenReturn(Optional.empty());

        assertThrows(EnclosureNotFoundException.class, () ->
                transferService.transferAnimal(animalId, invalidEnclosureId));
    }

    @Test
    void transferAnimal_FromWildToEnclosure_WorksCorrectly() {
        Long animalId = 1L;
        Long targetEnclosureId = 2L;
        Animal animal = new Animal();
        animal.setId(animalId);
        animal.setEnclosure(null);
        animal.setType(AnimalType.HERBIVORE);

        Enclosure targetEnclosure = new Enclosure();
        targetEnclosure.setId(targetEnclosureId);
        targetEnclosure.setMaxCapacity(new EnclosureMaxCapacity(5));
        targetEnclosure.setType(EnclosureType.HERBIVORE_ENCLOSURE);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(animal));
        when(enclosureRepository.findById(targetEnclosureId)).thenReturn(Optional.of(targetEnclosure));

        transferService.transferAnimal(animalId, targetEnclosureId);

        assertNull(captureEvent().getFromEnclosureId());
        assertEquals(targetEnclosureId, captureEvent().getToEnclosureId());
    }

    private AnimalMovedEvent captureEvent() {
        ArgumentCaptor<AnimalMovedEvent> eventCaptor = ArgumentCaptor.forClass(AnimalMovedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        return eventCaptor.getValue();
    }
}