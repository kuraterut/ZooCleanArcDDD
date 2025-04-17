package org.kuraterut.zoohm2hse.infrastructure.repositories;

import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByType(AnimalType type);

    @Query("SELECT a FROM Animal a WHERE a.enclosure.id = :enclosureId")
    List<Animal> findByEnclosureId(@Param("enclosureId") Long enclosureId);

    List<Animal> findByHealthyFalse();
}