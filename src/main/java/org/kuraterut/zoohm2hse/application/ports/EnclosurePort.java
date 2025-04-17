package org.kuraterut.zoohm2hse.application.ports;

import org.kuraterut.zoohm2hse.domain.Enclosure;

import java.util.List;

public interface EnclosurePort {
    Enclosure createEnclosure(Enclosure enclosure);
    Enclosure getEnclosureById(Long id);
    List<Enclosure> getAllEnclosures();
    List<Enclosure> getAvailableEnclosures();
    void deleteEnclosure(Long id);
    Enclosure updateEnclosure(Long id, Enclosure enclosureDetails);
}
