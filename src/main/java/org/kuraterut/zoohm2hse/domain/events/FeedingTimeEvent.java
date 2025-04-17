package org.kuraterut.zoohm2hse.domain.events;

import lombok.Getter;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;
import org.springframework.context.ApplicationEvent;

import java.time.LocalTime;

@Getter
public class FeedingTimeEvent extends ApplicationEvent {
    private final Long scheduleId;
    private final Long animalId;
    private final FoodType foodType;
    private final LocalTime localTime;

    public FeedingTimeEvent(Long scheduleId, Long animalId, FoodType foodType, LocalTime localTime) {
        super(scheduleId);
        this.scheduleId = scheduleId;
        this.animalId = animalId;
        this.foodType = foodType;
        this.localTime = localTime;
    }
}