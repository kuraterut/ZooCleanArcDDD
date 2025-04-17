package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.services.ZooStatisticsService;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureMaxCapacity;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureType;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ZooStatisticsServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private EnclosureRepository enclosureRepository;

    @InjectMocks
    private ZooStatisticsService statisticsService;

    private Animal buildPredatorAnimal(){
        Animal animal = new Animal();
        animal.setType(AnimalType.PREDATOR);
        return animal;
    }

    private Animal buildHerbivoreAnimal(){
        Animal animal = new Animal();
        animal.setType(AnimalType.HERBIVORE);
        return animal;
    }

    @Test
    void getZooStatistics_ReturnsCorrectData() {
        when(animalRepository.count()).thenReturn(5L);
        when(animalRepository.findByHealthyFalse()).thenReturn(List.of(
                new Animal(), new Animal()
        ));

        Enclosure predatorEnclosure = new Enclosure();
        predatorEnclosure.setType(EnclosureType.PREDATOR_ENCLOSURE);
        predatorEnclosure.setMaxCapacity(new EnclosureMaxCapacity(5));
        predatorEnclosure.addAnimal(buildPredatorAnimal());
        predatorEnclosure.addAnimal(buildPredatorAnimal());

        Enclosure herbivoreEnclosure = new Enclosure();
        herbivoreEnclosure.setType(EnclosureType.HERBIVORE_ENCLOSURE);
        herbivoreEnclosure.setMaxCapacity(new EnclosureMaxCapacity(3));
        herbivoreEnclosure.addAnimal(buildHerbivoreAnimal());

        when(enclosureRepository.count()).thenReturn(2L);
        when(enclosureRepository.findAll()).thenReturn(List.of(
                predatorEnclosure, herbivoreEnclosure
        ));

        Map<String, Object> stats = statisticsService.getZooStatistics();

        assertEquals(5L, stats.get("totalAnimals"));
        assertEquals(2L, stats.get("totalEnclosures"));
        assertEquals(2, stats.get("sickAnimalsCount"));


        @SuppressWarnings("unchecked")
        Map<String, Object> predatorStats = (Map<String, Object>) stats.get("predator_enclosure_EnclosureStats");
        assertEquals(2, predatorStats.get("currentAnimals"));
        assertEquals(5, predatorStats.get("maxCapacity"));
        assertEquals(3, predatorStats.get("availableSpace"));

        @SuppressWarnings("unchecked")
        Map<String, Object> herbivoreStats = (Map<String, Object>) stats.get("herbivore_enclosure_EnclosureStats");
        assertEquals(1, herbivoreStats.get("currentAnimals"));
    }

    @Test
    void getZooStatistics_EmptyZoo_ReturnsZeroValues() {
        when(animalRepository.count()).thenReturn(0L);
        when(animalRepository.findByHealthyFalse()).thenReturn(Collections.emptyList());
        when(enclosureRepository.count()).thenReturn(0L);
        when(enclosureRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> stats = statisticsService.getZooStatistics();

        assertEquals(0L, stats.get("totalAnimals"));
        assertEquals(0L, stats.get("totalEnclosures"));
        assertEquals(0, stats.get("sickAnimalsCount"));
        assertFalse(stats.containsKey("predator_enclosureStats"));
    }
}