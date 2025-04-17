package org.kuraterut.zoohm2hse.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnimalHealEvent extends ApplicationEvent {
    private final Long animalId;

    public AnimalHealEvent(Long animalId) {
        super(animalId);
        this.animalId = animalId;
    }
}
