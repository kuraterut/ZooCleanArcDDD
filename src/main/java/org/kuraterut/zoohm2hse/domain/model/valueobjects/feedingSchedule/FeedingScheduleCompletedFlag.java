package org.kuraterut.zoohm2hse.domain.model.valueobjects.feedingSchedule;

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

    protected FeedingScheduleCompletedFlag() {
        this.value = false;
    }

    public FeedingScheduleCompletedFlag markCompleted() {
        return new FeedingScheduleCompletedFlag(true);
    }
}