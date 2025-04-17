package org.kuraterut.zoohm2hse.infrastructure.repositories;

import org.kuraterut.zoohm2hse.domain.model.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.feedingSchedule.FeedingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface FeedingScheduleRepository extends JpaRepository<FeedingSchedule, Long> {

    List<FeedingSchedule> findByFeedingTime(FeedingTime feedingTime);

    @Query("SELECT fs FROM FeedingSchedule fs WHERE fs.feedingTime.value >= :currentTime AND fs.isCompleted.value = false")
    List<FeedingSchedule> findUpcomingFeedings(@Param("currentTime") LocalTime currentTime);
}