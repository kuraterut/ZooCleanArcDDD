package org.kuraterut.zoohm2hse.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.services.AnimalTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class AnimalTransferController {
    private final AnimalTransferService transferService;

    @PostMapping
    public ResponseEntity<Void> transferAnimal(
            @RequestParam Long animalId,
            @RequestParam Long enclosureId) {
        transferService.transferAnimal(animalId, enclosureId);
        return ResponseEntity.noContent().build();
    }
}