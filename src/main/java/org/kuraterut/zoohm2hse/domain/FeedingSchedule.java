package org.kuraterut.zoohm2hse.domain;

// domain/FeedingSchedule.java
import jakarta.persistence.*;
import lombok.*;
import org.kuraterut.zoohm2hse.application.exceptions.InvalidNewTimeForFeedingSchedule;
import org.kuraterut.zoohm2hse.domain.valueobjects.FoodType;

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

    @Column(nullable = false)
    private LocalTime feedingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodType foodType;

    @Column(nullable = false)
    private boolean isCompleted = false;


    // Доменные методы
    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void reschedule(LocalTime newTime) {
        if (newTime.isBefore(LocalTime.now())) {
            throw new InvalidNewTimeForFeedingSchedule("Новое время кормления не может быть в прошлом");
        }
        this.feedingTime = newTime;
        this.isCompleted = false;
        System.out.println("Кормление перенесено на " + newTime);
    }
}