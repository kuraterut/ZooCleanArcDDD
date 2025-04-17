package org.kuraterut.zoohm2hse.infrastructure.listeners;

import lombok.extern.slf4j.Slf4j;
import org.kuraterut.zoohm2hse.domain.events.AnimalMovedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalMovedEventListener {

    @EventListener
    public void handleAnimalMovedEvent(AnimalMovedEvent event) {
        log.info("Animal {} moved from enclosure {} to enclosure {}",
                event.getAnimalId(),
                event.getFromEnclosureId() != null ? event.getFromEnclosureId() : "none",
                event.getToEnclosureId());
    }
}