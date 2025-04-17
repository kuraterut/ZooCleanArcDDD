package org.kuraterut.zoohm2hse.infrastructure.repositories;

import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.domain.valueobjects.EnclosureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnclosureRepository extends JpaRepository<Enclosure, Long> {

    // Найти вольеры по типу
    List<Enclosure> findByType(EnclosureType type);

    // Найти незаполненные вольеры
    @Query("SELECT e FROM Enclosure e WHERE SIZE(e.animals) < e.maxCapacity")
    List<Enclosure> findAvailableEnclosures();
}