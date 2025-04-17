package org.kuraterut.zoohm2hse.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class AnimalFedEvent extends ApplicationEvent {
    private final Long animalId;
    private final LocalDateTime timestamp;

    public AnimalFedEvent(Long animalId) {
        super(animalId);
        this.animalId = animalId;
        this.timestamp = LocalDateTime.now();
    }
}