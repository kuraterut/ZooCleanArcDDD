package org.kuraterut.zoohm2hse.application.services;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalNotFoundException;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureNotFoundException;
import org.kuraterut.zoohm2hse.application.ports.AnimalTransferPort;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.events.AnimalMovedEvent;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnimalTransferService implements AnimalTransferPort {
    private final AnimalRepository animalRepository;
    private final EnclosureRepository enclosureRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void transferAnimal(Long animalId, Long targetEnclosureId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found by id: " + animalId));

        Enclosure currentEnclosure = animal.getEnclosure();
        Enclosure targetEnclosure = enclosureRepository.findById(targetEnclosureId)
                .orElseThrow(() -> new EnclosureNotFoundException("Enclosure not found by id: " + targetEnclosureId));


        if (currentEnclosure != null) {
            currentEnclosure.removeAnimal(animal);
        }

        targetEnclosure.addAnimal(animal);
        animal.setEnclosure(targetEnclosure);

        animalRepository.save(animal);
        enclosureRepository.save(targetEnclosure);

        eventPublisher.publishEvent(new AnimalMovedEvent(
                animal.getId(),
                currentEnclosure != null ? currentEnclosure.getId() : null,
                targetEnclosureId
        ));
    }
}