package org.kuraterut.zoohm2hse.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.Enclosure;
import org.kuraterut.zoohm2hse.domain.valueobjects.AnimalType;
import org.kuraterut.zoohm2hse.domain.valueobjects.EnclosureType;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.Gender;

import java.time.LocalDate;

@Getter
public class AnimalResponse {
    private Long id;
    private String name;
    private AnimalType type;
    private LocalDate birthDate;
    private Gender gender;
    private FoodType favoriteFood;
    private boolean healthy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EnclosureInfo enclosure;

    public AnimalResponse(Animal animal) {
        this.id = animal.getId();
        this.name = animal.getName();
        this.type = animal.getType();
        this.birthDate = animal.getBirthDate();
        this.gender = animal.getGender();
        this.favoriteFood = animal.getFavoriteFood();
        this.healthy = animal.isHealthy();
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
            this(enclosure.getId(), enclosure.getType(), enclosure.getMaxCapacity());
        }
    }
}