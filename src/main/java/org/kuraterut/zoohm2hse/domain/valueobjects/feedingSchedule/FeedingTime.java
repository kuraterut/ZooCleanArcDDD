package org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;
import org.kuraterut.zoohm2hse.application.exceptions.InvalidFeedingTimeException;

import java.time.LocalTime;

@Embeddable
@Value
public class FeedingTime {
    @Column(name = "feeding_time")
    LocalTime value;

    public FeedingTime(LocalTime value) {
        if (value == null) {
            throw new InvalidFeedingTimeException("Время кормления не может быть null");
        }
        this.value = value;
    }

    protected FeedingTime() { // Для JPA
        this.value = null;
    }

    public boolean isExpired() {
        if(value == null) throw new InvalidFeedingTimeException("Время кормления не может быть null");
        return value.isBefore(LocalTime.now());
    }
}