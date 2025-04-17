package org.kuraterut.zoohm2hse.infrastructure.listeners;

import lombok.extern.slf4j.Slf4j;
import org.kuraterut.zoohm2hse.domain.events.AnimalFedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalFedEventListener {
    @EventListener
    public void handleAnimalFedEvent(AnimalFedEvent event) {
        log.info("Animal with id {} has fed!", event.getAnimalId());
    }
}
