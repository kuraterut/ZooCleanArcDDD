package org.kuraterut.zoohm2hse.application.ports;

import org.kuraterut.zoohm2hse.domain.model.FeedingSchedule;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateFeedingScheduleRequest;

import java.util.List;

public interface FeedingSchedulePort {
    FeedingSchedule createSchedule(CreateFeedingScheduleRequest request);
    FeedingSchedule getScheduleById(Long id);
    List<FeedingSchedule> getAllSchedules();
    List<FeedingSchedule> getUpcomingFeedings();
    void deleteSchedule(Long id);
    FeedingSchedule updateSchedule(Long id, FeedingSchedule scheduleDetails);
    void completeFeeding(Long scheduleId);
}
