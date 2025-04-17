package org.kuraterut.zoohm2hse.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.ports.FeedingSchedulePort;
import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateFeedingScheduleRequest;
import org.kuraterut.zoohm2hse.presentation.dto.response.FeedingScheduleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feeding-schedules")
@RequiredArgsConstructor
public class FeedingScheduleController {
    private final FeedingSchedulePort schedulePort;

    @PostMapping
    public ResponseEntity<FeedingScheduleResponse> createSchedule(
            @RequestBody CreateFeedingScheduleRequest request) {
        FeedingScheduleResponse response = new FeedingScheduleResponse(schedulePort.createSchedule(request));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedingScheduleResponse> getSchedule(@PathVariable Long id) {
        FeedingScheduleResponse response = new FeedingScheduleResponse(schedulePort.getScheduleById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FeedingScheduleResponse>> getAllSchedules() {
        List<FeedingScheduleResponse> responses = schedulePort.getAllSchedules()
                .stream()
                .map(FeedingScheduleResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedingScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @RequestBody FeedingSchedule scheduleDetails) {
        FeedingScheduleResponse response = new FeedingScheduleResponse(schedulePort.updateSchedule(id, scheduleDetails));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        schedulePort.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FeedingScheduleResponse>> getUpcomingFeedings() {
        List<FeedingScheduleResponse> responses = schedulePort.getUpcomingFeedings()
                .stream()
                .map(FeedingScheduleResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/complete/{id}")
    public ResponseEntity<Void> completeFeeding(@PathVariable Long id) {
        schedulePort.completeFeeding(id);
        return ResponseEntity.noContent().build();
    }
}