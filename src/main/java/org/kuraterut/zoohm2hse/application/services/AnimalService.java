package org.kuraterut.zoohm2hse.application.services;


import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalNotFoundException;
import org.kuraterut.zoohm2hse.application.ports.AnimalPort;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.domain.events.AnimalFedEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalHealEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalSickEvent;
import org.kuraterut.zoohm2hse.infrastructure.repositories.AnimalRepository;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateAnimalRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimalService implements AnimalPort {
    private final AnimalRepository animalRepository;
    private final EnclosureService enclosureService;
    private final ApplicationEventPublisher eventPublisher;

    public Animal createAnimal(CreateAnimalRequest request) {
        Animal animal = new Animal();
        animal.setName(request.getName());
        animal.setType(request.getType());
        animal.setBirthDate(request.getBirthDate());
        animal.setGender(request.getGender());
        animal.setFavoriteFood(request.getFavoriteFood());
        animal.setHealthy(request.isHealthy());
        Enclosure enclosure = enclosureService.getEnclosureById(request.getEnclosureId());
        animal.setEnclosure(enclosure);
        return animalRepository.save(animal);
    }

    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found with id: " + id));
    }

    public List<Animal> getAnimalsByEnclosureId(Long id) {
        return animalRepository.findByEnclosureId(id);
    }

    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    public void deleteAnimal(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new AnimalNotFoundException("Animal not found with id: " + id);
        }
        animalRepository.deleteById(id);
    }

    public Animal updateAnimal(Long id, Animal animalDetails) {
        if (!animalRepository.existsById(id)) {
            throw new AnimalNotFoundException("Animal not found with id: " + id);
        }
        Animal animal = getAnimalById(id);
        animal.setName(animalDetails.getName());
        animal.setType(animalDetails.getType());
        animal.setBirthDate(animalDetails.getBirthDate());
        animal.setGender(animalDetails.getGender());
        animal.setFavoriteFood(animalDetails.getFavoriteFood());
        animal.setHealthy(animalDetails.isHealthy());
        return animalRepository.save(animal);
    }

    @Transactional
    public void feedAnimal(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found by id: " + animalId));
        animal.feed();
        animalRepository.save(animal);
        eventPublisher.publishEvent(new AnimalFedEvent(animalId));
    }

    @Transactional
    public void healAnimal(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new AnimalNotFoundException("Animal not found by id: " + animalId));
        animal.heal();
        animalRepository.save(animal);
        eventPublisher.publishEvent(new AnimalHealEvent(animalId));
    }

    @Transactional
    public void sickAnimal(Long animalId) {
        Animal animal = getAnimalById(animalId);
        animal.markAsSick();
        animalRepository.save(animal);
        eventPublisher.publishEvent(new AnimalSickEvent(animalId));
    }

    public List<Animal> getSickAnimals(){
        return animalRepository.findByHealthyFalse();
    }
}
