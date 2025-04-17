package org.kuraterut.zoohm2hse.presentation.dto.response;

import lombok.Getter;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.Enclosure;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.enclosure.EnclosureType;

import java.util.List;

@Getter
public class EnclosureResponse {
    private Long id;
    private EnclosureType type;
    private int maxCapacity;
    private List<AnimalInfo> animals;

    public EnclosureResponse(Enclosure enclosure) {
        this.id = enclosure.getId();
        this.type = enclosure.getType();
        this.maxCapacity = enclosure.getMaxCapacity().getValue();
        this.animals = enclosure.getAnimals().stream()
                .map(AnimalInfo::new)
                .toList();
    }

    @Getter
    public static class AnimalInfo {
        private Long id;
        private String name;
        private AnimalType type;

        public AnimalInfo(Animal animal) {
            this.id = animal.getId();
            this.name = animal.getName().getValue();
            this.type = animal.getType();
        }
    }
}