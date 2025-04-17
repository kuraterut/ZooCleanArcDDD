package org.kuraterut.zoohm2hse.application.services;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureIsNotEmptyException;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureNotFoundException;
import org.kuraterut.zoohm2hse.application.ports.EnclosurePort;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.infrastructure.repositories.EnclosureRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnclosureService implements EnclosurePort {
    private final EnclosureRepository enclosureRepository;

    public Enclosure createEnclosure(Enclosure enclosure) {
        return enclosureRepository.save(enclosure);
    }

    public Enclosure getEnclosureById(Long id) {
        return enclosureRepository.findById(id)
                .orElseThrow(() -> new EnclosureNotFoundException("Enclosure not found with id: " + id));
    }

    public List<Enclosure> getAllEnclosures() {
        return enclosureRepository.findAll();
    }

    public List<Enclosure> getAvailableEnclosures() {
        return enclosureRepository.findAvailableEnclosures();
    }

    public void deleteEnclosure(Long id) {
        Enclosure enclosure = getEnclosureById(id);
        if (!enclosure.getAnimals().isEmpty()) {
            throw new EnclosureIsNotEmptyException("Can't delete enclosure. Enclosure is not empty");
        }
        enclosureRepository.deleteById(id);
    }

    public Enclosure updateEnclosure(Long id, Enclosure enclosureDetails) {
        if (!enclosureRepository.existsById(id)) {
            throw new EnclosureNotFoundException("Enclosure not found with id: " + id);
        }
        Enclosure enclosure = getEnclosureById(id);
        enclosure.setType(enclosureDetails.getType());
        enclosure.setMaxCapacity(enclosureDetails.getMaxCapacity());
        return enclosureRepository.save(enclosure);
    }
}