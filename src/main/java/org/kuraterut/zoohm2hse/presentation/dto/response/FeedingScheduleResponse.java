package org.kuraterut.zoohm2hse.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedingScheduleResponse {
    private Long id;
    private AnimalResponse animal;
    private LocalTime feedingTime;
    private FoodType foodType;
    private boolean isCompleted;

    public FeedingScheduleResponse(final FeedingSchedule feedingSchedule) {
        this.id = feedingSchedule.getId();
        this.animal = new AnimalResponse(feedingSchedule.getAnimal());
        this.feedingTime = feedingSchedule.getFeedingTime();
        this.foodType = feedingSchedule.getFoodType();
        this.isCompleted = feedingSchedule.isCompleted();
    }
}
