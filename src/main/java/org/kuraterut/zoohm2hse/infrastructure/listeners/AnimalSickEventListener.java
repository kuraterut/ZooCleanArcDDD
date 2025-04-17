package org.kuraterut.zoohm2hse.infrastructure.listeners;

import lombok.extern.slf4j.Slf4j;
import org.kuraterut.zoohm2hse.domain.events.AnimalHealEvent;
import org.kuraterut.zoohm2hse.domain.events.AnimalSickEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalSickEventListener {

    @EventListener
    public void handleAnimalSickEvent(AnimalSickEvent event) {
        log.info("Animal with id {} marked as sick", event.getAnimalId());
    }
}
