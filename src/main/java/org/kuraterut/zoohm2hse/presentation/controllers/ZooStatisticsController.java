package org.kuraterut.zoohm2hse.presentation.controllers;

import lombok.RequiredArgsConstructor;
import org.kuraterut.zoohm2hse.application.services.ZooStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class ZooStatisticsController {
    private final ZooStatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(statisticsService.getZooStatistics());
    }
}
