package org.kuraterut.zoohm2hse.domain.events;

import lombok.Getter;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingTime;
import org.springframework.context.ApplicationEvent;

import java.time.LocalTime;

@Getter
public class FeedingTimeEvent extends ApplicationEvent {
    private final Long scheduleId;
    private final Long animalId;
    private final FoodType foodType;
    private final LocalTime feedingTime;

    public FeedingTimeEvent(Long scheduleId, Long animalId, FoodType foodType, FeedingTime feedingTime) {
        super(scheduleId);
        this.scheduleId = scheduleId;
        this.animalId = animalId;
        this.foodType = foodType;
        this.feedingTime = feedingTime.getValue();
    }
}