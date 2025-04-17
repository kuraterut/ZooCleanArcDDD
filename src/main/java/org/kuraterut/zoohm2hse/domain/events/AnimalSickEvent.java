package org.kuraterut.zoohm2hse.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnimalSickEvent extends ApplicationEvent {
    private final Long animalId;

    public AnimalSickEvent(Long animalId) {
        super(animalId);
        this.animalId = animalId;
    }
}
