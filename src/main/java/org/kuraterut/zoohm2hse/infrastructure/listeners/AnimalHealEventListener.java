package org.kuraterut.zoohm2hse.infrastructure.listeners;

import lombok.extern.slf4j.Slf4j;
import org.kuraterut.zoohm2hse.domain.events.AnimalHealEvent;
import org.kuraterut.zoohm2hse.domain.events.FeedingTimeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnimalHealEventListener {

    @EventListener
    public void handleAnimalHealEvent(AnimalHealEvent event) {
        log.info("Animal with id {} was healed", event.getAnimalId());
    }
}