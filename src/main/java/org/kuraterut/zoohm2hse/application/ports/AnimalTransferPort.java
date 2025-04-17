package org.kuraterut.zoohm2hse.application.ports;

public interface AnimalTransferPort {
    void transferAnimal(Long animalId, Long targetEnclosureId);
}
