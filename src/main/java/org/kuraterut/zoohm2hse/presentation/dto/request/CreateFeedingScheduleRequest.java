package org.kuraterut.zoohm2hse.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeedingScheduleRequest {
    private Long animalId;
    private LocalTime feedingTime;
    private FoodType foodType;
}
