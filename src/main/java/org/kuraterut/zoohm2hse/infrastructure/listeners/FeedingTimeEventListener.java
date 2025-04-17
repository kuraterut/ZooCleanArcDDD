package org.kuraterut.zoohm2hse.infrastructure.listeners;

import lombok.extern.slf4j.Slf4j;
import org.kuraterut.zoohm2hse.domain.events.FeedingTimeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeedingTimeEventListener {

    @EventListener
    public void handleFeedingTimeEvent(FeedingTimeEvent event) {
        log.info("Feeding time for animal {} (schedule {}). Food type: {}",
                event.getAnimalId(),
                event.getScheduleId(),
                event.getFoodType());
    }
}