package org.kuraterut.zoohm2hse.domain;

import java.util.List;

// domain/Enclosure.java
import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.zoohm2hse.application.exceptions.AnimalTypeIsNotCompatibleException;
import org.kuraterut.zoohm2hse.application.exceptions.EnclosureIsFullException;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.valueobjects.enclosure.EnclosureMaxCapacity;
import org.kuraterut.zoohm2hse.domain.valueobjects.enclosure.EnclosureType;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enclosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EnclosureType type;

    @Embedded
    @Column(nullable = false)
    private EnclosureMaxCapacity maxCapacity;

    @OneToMany(mappedBy = "enclosure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Animal> animals = new ArrayList<>();


    // Основные методы
    public void addAnimal(Animal animal) {
        if (animals.size() >= maxCapacity.getValue()) {
            throw new EnclosureIsFullException("Can't move animal. Target enclosure is full");
        }
        if (!isCompatible(animal)) {
            throw new AnimalTypeIsNotCompatibleException("Can't move animal. Animal type is not compatible");
        }
        animals.add(animal);
        animal.setEnclosure(this);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void clean() {
        System.out.println("Вольер " + id + " очищен");
    }

    private boolean isCompatible(Animal animal) {
        // Проверяем совместимость типа вольера и животного
        return (this.type == EnclosureType.PREDATOR_ENCLOSURE && animal.getType() == AnimalType.PREDATOR) ||
                (this.type == EnclosureType.HERBIVORE_ENCLOSURE && animal.getType() == AnimalType.HERBIVORE) ||
                (this.type == EnclosureType.AVIARY && animal.getType() == AnimalType.BIRD) ||
                (this.type == EnclosureType.AQUARIUM && animal.getType() == AnimalType.AQUATIC);
    }
}