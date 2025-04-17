package org.kuraterut.zoohm2hse.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.ports.EnclosurePort;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.presentation.dto.response.EnclosureResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enclosures")
@RequiredArgsConstructor
public class EnclosureController {
    private final EnclosurePort enclosurePort;

    @PostMapping
    public ResponseEntity<EnclosureResponse> createEnclosure(@RequestBody Enclosure enclosure) {
        EnclosureResponse response = new EnclosureResponse(enclosurePort.createEnclosure(enclosure));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnclosureResponse> getEnclosure(@PathVariable Long id) {
        EnclosureResponse response = new EnclosureResponse(enclosurePort.getEnclosureById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EnclosureResponse>> getAllEnclosures() {
        List<EnclosureResponse> responses = enclosurePort.getAllEnclosures()
                .stream()
                .map(EnclosureResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnclosureResponse> updateEnclosure(
            @PathVariable Long id,
            @RequestBody Enclosure enclosureDetails) {
        EnclosureResponse response = new EnclosureResponse(enclosurePort.updateEnclosure(id, enclosureDetails));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnclosure(@PathVariable Long id) {
        enclosurePort.deleteEnclosure(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<EnclosureResponse>> getAvailableEnclosures() {
        List<EnclosureResponse> responses = enclosurePort.getAvailableEnclosures()
                .stream()
                .map(EnclosureResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}