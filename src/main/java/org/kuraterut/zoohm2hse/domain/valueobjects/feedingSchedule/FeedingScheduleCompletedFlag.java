package org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;

@Embeddable
@Value
public class FeedingScheduleCompletedFlag {
    @Column(name = "is_completed")
    boolean value;

    public FeedingScheduleCompletedFlag(boolean value) {
        this.value = value;
    }

    protected FeedingScheduleCompletedFlag() { // Для JPA
        this.value = false;
    }

    public FeedingScheduleCompletedFlag markCompleted() {
        return new FeedingScheduleCompletedFlag(true);
    }
}