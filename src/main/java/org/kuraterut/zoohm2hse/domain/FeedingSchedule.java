package org.kuraterut.zoohm2hse.domain;

import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.zoohm2hse.application.exceptions.InvalidFeedingTimeException;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingScheduleCompletedFlag;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingTime;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Embedded
    @Column(nullable = false)
    private FeedingTime feedingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;

    @Embedded
    @Column(nullable = false)
    private FeedingScheduleCompletedFlag isCompleted;


    // Доменные методы
    public void markAsCompleted() {
        this.isCompleted = new FeedingScheduleCompletedFlag(true);
    }

    public void reschedule(FeedingTime newTime) {
        if (newTime.getValue().isBefore(LocalTime.now())) {
            throw new InvalidFeedingTimeException("Новое время кормления не может быть в прошлом");
        }
        this.feedingTime = newTime;
        this.isCompleted = new FeedingScheduleCompletedFlag(false);
        System.out.println("Кормление перенесено на " + newTime.getValue());
    }
}