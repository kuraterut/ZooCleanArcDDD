package org.kuraterut.zoohm2hse.application.ports;

import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateEnclosureRequest;

import java.util.List;

public interface EnclosurePort {
    Enclosure createEnclosure(CreateEnclosureRequest request);
    Enclosure getEnclosureById(Long id);
    List<Enclosure> getAllEnclosures();
    List<Enclosure> getAvailableEnclosures();
    void deleteEnclosure(Long id);
    Enclosure updateEnclosure(Long id, Enclosure enclosureDetails);
}
