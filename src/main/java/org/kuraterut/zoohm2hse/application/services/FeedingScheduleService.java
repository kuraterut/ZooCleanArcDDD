package org.kuraterut.zoohm2hse.application.services;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.exceptions.FeedingScheduleNotFoundException;
import org.kuraterut.zoohm2hse.application.ports.FeedingSchedulePort;
import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.infrastructure.repositories.FeedingScheduleRepository;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateFeedingScheduleRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedingScheduleService implements FeedingSchedulePort {
    private final FeedingScheduleRepository scheduleRepository;
    private final AnimalService animalService;

    public FeedingSchedule createSchedule(CreateFeedingScheduleRequest request) {
        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setAnimal(animalService.getAnimalById(request.getAnimalId()));
        schedule.setFeedingTime(request.getFeedingTime());
        schedule.setFoodType(request.getFoodType());
        schedule.setCompleted(false);
        return scheduleRepository.save(schedule);
    }

    public FeedingSchedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new FeedingScheduleNotFoundException("Schedule not found with id: " + id));
    }

    public List<FeedingSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<FeedingSchedule> getUpcomingFeedings() {
        return scheduleRepository.findUpcomingFeedings(LocalTime.now());
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new FeedingScheduleNotFoundException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    public FeedingSchedule updateSchedule(Long id, FeedingSchedule scheduleDetails) {
        if (!scheduleRepository.existsById(id)) {
            throw new FeedingScheduleNotFoundException("Schedule not found with id: " + id);
        }
        FeedingSchedule schedule = getScheduleById(id);
        schedule.setAnimal(scheduleDetails.getAnimal());
        schedule.setFeedingTime(scheduleDetails.getFeedingTime());
        schedule.setFoodType(scheduleDetails.getFoodType());
        schedule.setCompleted(scheduleDetails.isCompleted());
        return scheduleRepository.save(schedule);
    }
    public void completeFeeding(Long scheduleId) {
        FeedingSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new FeedingScheduleNotFoundException("Schedule not found with id: " + scheduleId));
        schedule.markAsCompleted();
        scheduleRepository.save(schedule);
    }
}