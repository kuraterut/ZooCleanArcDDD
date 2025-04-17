package org.kuraterut.zoohm2hse.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.valueobjects.enclosure.EnclosureType;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.Gender;

import java.time.LocalDate;

@Getter
public class AnimalResponse {
    private final Long id;
    private final String name;
    private final AnimalType type;
    private final LocalDate birthDate;
    private final Gender gender;
    private final FoodType favoriteFood;
    private final String healthy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final EnclosureInfo enclosure;

    public AnimalResponse(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName().getValue();
        this.type = animal.getType();
        this.birthDate = animal.getBirthDate().getValue();
        this.gender = animal.getGender();
        this.favoriteFood = animal.getFavoriteFood();
        this.healthy = animal.getHealthy().toStatus();
        this.enclosure = animal.getEnclosure() != null
                ? new EnclosureInfo(animal.getEnclosure())
                : null;
    }

    @Getter
    @AllArgsConstructor
    public static class EnclosureInfo {
        private Long id;
        private EnclosureType type;
        private int maxCapacity;

        public EnclosureInfo(Enclosure enclosure) {
            this(enclosure.getId(), enclosure.getType(), enclosure.getMaxCapacity().getValue());
        }
    }
}