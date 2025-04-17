package org.kuraterut.zoohm2hse.application.services;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.ports.ZooStatisticsPort;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZooStatisticsService implements ZooStatisticsPort {
    private final AnimalRepository animalRepository;
    private final EnclosureRepository enclosureRepository;

    public Map<String, Object> getZooStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalAnimals", animalRepository.count());
        stats.put("totalEnclosures", enclosureRepository.count());
        stats.put("sickAnimalsCount", animalRepository.findByHealthyFalse().size());

        enclosureRepository.findAll().forEach(enclosure -> {
            String key = enclosure.getType().toString().toLowerCase() + "EnclosureStats";
            Map<String, Object> enclosureStats = new HashMap<>();
            enclosureStats.put("currentAnimals", enclosure.getAnimals().size());
            enclosureStats.put("maxCapacity", enclosure.getMaxCapacity());
            enclosureStats.put("availableSpace", enclosure.getMaxCapacity() - enclosure.getAnimals().size());
            stats.put(key, enclosureStats);
        });

        return stats;
    }
}