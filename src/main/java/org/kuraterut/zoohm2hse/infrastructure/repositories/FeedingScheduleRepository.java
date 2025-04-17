package org.kuraterut.zoohm2hse.infrastructure.repositories;

import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface FeedingScheduleRepository extends JpaRepository<FeedingSchedule, Long> {

    // Найти расписания по времени кормления
    List<FeedingSchedule> findByFeedingTime(FeedingTime feedingTime);

    // Найти предстоящие кормления
    @Query("SELECT fs FROM FeedingSchedule fs WHERE fs.feedingTime.value >= :currentTime AND fs.isCompleted.value = false")
    List<FeedingSchedule> findUpcomingFeedings(@Param("currentTime") LocalTime currentTime);
}