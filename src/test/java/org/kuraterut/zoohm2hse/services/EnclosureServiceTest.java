package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureIsNotEmptyException;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureNotFoundException;
import org.kuraterut.zoohm2hse.application.services.EnclosureService;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureMaxCapacity;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureType;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateEnclosureRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnclosureServiceTest {

    @Mock
    private EnclosureRepository enclosureRepository;

    @InjectMocks
    private EnclosureService enclosureService;

    @Test
    void createEnclosure_ValidRequest_ReturnsSavedEnclosure() {
        CreateEnclosureRequest request = new CreateEnclosureRequest(
                EnclosureType.PREDATOR_ENCLOSURE, 5
        );

        Enclosure expectedEnclosure = new Enclosure();
        expectedEnclosure.setType(request.getType());
        expectedEnclosure.setMaxCapacity(new EnclosureMaxCapacity(request.getMaxCapacity()));

        when(enclosureRepository.save(any(Enclosure.class))).thenReturn(expectedEnclosure);

        Enclosure result = enclosureService.createEnclosure(request);

        assertNotNull(result);
        assertEquals(EnclosureType.PREDATOR_ENCLOSURE, result.getType());
        assertEquals(5, result.getMaxCapacity().getValue());
        verify(enclosureRepository).save(any(Enclosure.class));
    }

    @Test
    void getEnclosureById_ExistingId_ReturnsEnclosure() {
        Long enclosureId = 1L;
        Enclosure expectedEnclosure = new Enclosure();
        expectedEnclosure.setId(enclosureId);

        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(expectedEnclosure));

        Enclosure result = enclosureService.getEnclosureById(enclosureId);

        assertEquals(enclosureId, result.getId());
    }

    @Test
    void getEnclosureById_NonExistingId_ThrowsException() {
        Long enclosureId = 999L;
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.empty());

        assertThrows(EnclosureNotFoundException.class, () ->
                enclosureService.getEnclosureById(enclosureId));
    }

    @Test
    void getAllEnclosures_ReturnsAllEnclosures() {
        List<Enclosure> expectedEnclosures = Arrays.asList(
                new Enclosure(), new Enclosure()
        );
        when(enclosureRepository.findAll()).thenReturn(expectedEnclosures);

        List<Enclosure> result = enclosureService.getAllEnclosures();

        assertEquals(2, result.size());
    }

    @Test
    void getAvailableEnclosures_ReturnsNonFullEnclosures() {
        Enclosure availableEnclosure = new Enclosure();
        availableEnclosure.setMaxCapacity(new EnclosureMaxCapacity(3));
        availableEnclosure.setAnimals(List.of(new Animal()));

        when(enclosureRepository.findAvailableEnclosures()).thenReturn(List.of(availableEnclosure));

        List<Enclosure> result = enclosureService.getAvailableEnclosures();

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getMaxCapacity().getValue());
    }

    @Test
    void deleteEnclosure_EmptyEnclosure_DeletesSuccessfully() {
        Long enclosureId = 1L;
        Enclosure emptyEnclosure = new Enclosure();
        emptyEnclosure.setAnimals(new ArrayList<>());

        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(emptyEnclosure));

        enclosureService.deleteEnclosure(enclosureId);

        verify(enclosureRepository).deleteById(enclosureId);
    }

    @Test
    void deleteEnclosure_NonEmptyEnclosure_ThrowsException() {
        Long enclosureId = 1L;
        Enclosure nonEmptyEnclosure = new Enclosure();
        nonEmptyEnclosure.setAnimals(List.of(new Animal()));

        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(nonEmptyEnclosure));

        assertThrows(EnclosureIsNotEmptyException.class, () ->
                enclosureService.deleteEnclosure(enclosureId));
        verify(enclosureRepository, never()).deleteById(any());
    }

    @Test
    void updateEnclosure_ValidData_ReturnsUpdatedEnclosure() {
        Long enclosureId = 1L;
        Enclosure existingEnclosure = new Enclosure();
        existingEnclosure.setId(enclosureId);
        existingEnclosure.setType(EnclosureType.HERBIVORE_ENCLOSURE);
        existingEnclosure.setMaxCapacity(new EnclosureMaxCapacity(3));

        Enclosure updatedDetails = new Enclosure();
        updatedDetails.setType(EnclosureType.AVIARY);
        updatedDetails.setMaxCapacity(new EnclosureMaxCapacity(5));

        when(enclosureRepository.existsById(enclosureId)).thenReturn(true);
        when(enclosureRepository.findById(enclosureId)).thenReturn(Optional.of(existingEnclosure));
        when(enclosureRepository.save(any(Enclosure.class))).thenReturn(existingEnclosure);

        Enclosure result = enclosureService.updateEnclosure(enclosureId, updatedDetails);

        assertEquals(EnclosureType.AVIARY, result.getType());
        assertEquals(5, result.getMaxCapacity().getValue());
    }

    @Test
    void updateEnclosure_NonExistingId_ThrowsException() {
        Long enclosureId = 999L;
        when(enclosureRepository.existsById(enclosureId)).thenReturn(false);

        assertThrows(EnclosureNotFoundException.class, () ->
                enclosureService.updateEnclosure(enclosureId, new Enclosure()));
    }
}