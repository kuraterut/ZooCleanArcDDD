package org.kuraterut.zoohm2hse.application.ports;

import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateAnimalRequest;

import java.util.List;

public interface AnimalPort {
    Animal createAnimal(CreateAnimalRequest request);
    Animal getAnimalById(Long id);
    List<Animal> getAnimalsByEnclosureId(Long id);
    List<Animal> getAllAnimals();
    void deleteAnimal(Long id);
    Animal updateAnimal(Long id, Animal animalDetails);
    void feedAnimal(Long animalId);
    void healAnimal(Long animalId);
    void sickAnimal(Long animalId);
    List<Animal> getSickAnimals();
}
