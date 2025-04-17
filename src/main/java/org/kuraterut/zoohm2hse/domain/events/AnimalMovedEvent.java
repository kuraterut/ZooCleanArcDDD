package org.kuraterut.zoohm2hse.domain.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AnimalMovedEvent extends ApplicationEvent {
    private final Long animalId;
    private final Long fromEnclosureId;
    private final Long toEnclosureId;

    public AnimalMovedEvent(Long animalId, Long fromEnclosureId, Long toEnclosureId) {
        super(animalId);
        this.animalId = animalId;
        this.fromEnclosureId = fromEnclosureId;
        this.toEnclosureId = toEnclosureId;
    }
}