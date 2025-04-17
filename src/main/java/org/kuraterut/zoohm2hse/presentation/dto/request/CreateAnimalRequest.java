package org.kuraterut.zoohm2hse.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.AnimalType;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.Gender;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnimalRequest {
    private String name;
    private AnimalType type;
    private LocalDate birthDate;
    private Gender gender;
    private FoodType favoriteFood;
    private boolean healthy;
    private Long enclosureId;
}
