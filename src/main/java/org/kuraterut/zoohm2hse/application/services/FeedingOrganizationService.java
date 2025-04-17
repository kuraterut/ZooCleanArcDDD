package org.kuraterut.zoohm2hse.application.services;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.ports.FeedingOrganizationPort;
import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.events.FeedingTimeEvent;
import org.kuraterut.zoohm2hse.infrastructure.repositories.FeedingScheduleRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedingOrganizationService implements FeedingOrganizationPort {
    private final FeedingScheduleRepository scheduleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 * * * * *") // Проверка каждую минуту
    public void checkFeedingTime() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        List<FeedingSchedule> schedules = scheduleRepository.findUpcomingFeedings(now);

        schedules.forEach(schedule -> {
            eventPublisher.publishEvent(new FeedingTimeEvent(
                    schedule.getId(),
                    schedule.getAnimal().getId(),
                    schedule.getFoodType(),
                    schedule.getFeedingTime()
            ));
        });
    }
}