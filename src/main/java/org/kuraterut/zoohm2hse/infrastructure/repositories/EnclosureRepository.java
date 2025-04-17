package org.kuraterut.zoohm2hse.infrastructure.repositories;

import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

    List<Enclosure> findByType(EnclosureType type);

    @Query("SELECT e FROM Enclosure e WHERE SIZE(e.animals) < e.maxCapacity.value")
    List<Enclosure> findAvailableEnclosures();
}