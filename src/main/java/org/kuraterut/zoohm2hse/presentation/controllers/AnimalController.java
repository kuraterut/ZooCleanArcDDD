package org.kuraterut.zoohm2hse.presentation.controllers;


import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.ports.AnimalPort;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateAnimalRequest;
import org.kuraterut.zoohm2hse.presentation.dto.response.AnimalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalPort animalPort;

    @PostMapping
    public ResponseEntity<AnimalResponse> createAnimal(@RequestBody CreateAnimalRequest request) {
        AnimalResponse response = new AnimalResponse(animalPort.createAnimal(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> getAnimal(@PathVariable Long id) {
        AnimalResponse response = new AnimalResponse(animalPort.getAnimalById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAllAnimals() {
        List<AnimalResponse> responses = animalPort.getAllAnimals()
                .stream()
                .map(AnimalResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> updateAnimal(
            @PathVariable Long id,
            @RequestBody Animal animalDetails) {
        AnimalResponse response = new AnimalResponse(animalPort.updateAnimal(id, animalDetails));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalPort.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/enclosure/{enclosureId}")
    public ResponseEntity<List<AnimalResponse>> getAnimalsByEnclosure(
            @PathVariable Long enclosureId) {
        List<AnimalResponse> responses = animalPort.getAnimalsByEnclosureId(enclosureId)
                .stream()
                .map(AnimalResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/heal/{id}")
    public ResponseEntity<Void> healAnimal(@PathVariable Long id) {
        animalPort.healAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sick/{id}")
    public ResponseEntity<Void> sickAnimal(@PathVariable Long id) {
        animalPort.sickAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/feed/{id}")
    public ResponseEntity<Void> feedAnimal(@PathVariable Long id) {
        animalPort.feedAnimal(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sick")
    public ResponseEntity<List<AnimalResponse>> getSickAnimals(){
        List<AnimalResponse> responses = animalPort.getSickAnimals()
                .stream()
                .map(AnimalResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}