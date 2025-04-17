package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.FeedingScheduleNotFoundException;
import org.kuraterut.zoohm2hse.application.services.AnimalService;
import org.kuraterut.zoohm2hse.application.services.FeedingScheduleService;
import org.kuraterut.zoohm2hse.domain.model.Animal;
import org.kuraterut.zoohm2hse.domain.model.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.feedingSchedule.FeedingScheduleCompletedFlag;
import org.kuraterut.zoohm2hse.domain.model.valueobjects.feedingSchedule.FeedingTime;
import org.kuraterut.zoohm2hse.infrastructure.repositories.FeedingScheduleRepository;
import org.kuraterut.zoohm2hse.presentation.dto.request.CreateFeedingScheduleRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FeedingScheduleServiceTest {

    @Mock
    private FeedingScheduleRepository scheduleRepository;

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private FeedingScheduleService feedingScheduleService;

    @Test
    void createSchedule_ValidRequest_ReturnsSavedSchedule() {
        Long animalId = 1L;
        LocalTime feedingTime = LocalTime.of(14, 30);
        CreateFeedingScheduleRequest request = new CreateFeedingScheduleRequest(
                animalId, feedingTime, FoodType.MEAT
        );

        Animal animal = new Animal();
        animal.setId(animalId);

        FeedingSchedule expectedSchedule = new FeedingSchedule();
        expectedSchedule.setAnimal(animal);
        expectedSchedule.setFeedingTime(new FeedingTime(feedingTime));
        expectedSchedule.setFoodType(FoodType.MEAT);
        expectedSchedule.setIsCompleted(new FeedingScheduleCompletedFlag(false));

        when(animalService.getAnimalById(animalId)).thenReturn(animal);
        when(scheduleRepository.save(any(FeedingSchedule.class))).thenReturn(expectedSchedule);

        FeedingSchedule result = feedingScheduleService.createSchedule(request);

        assertNotNull(result);
        assertEquals(animalId, result.getAnimal().getId());
        assertEquals(feedingTime, result.getFeedingTime().getValue());
        assertEquals(FoodType.MEAT, result.getFoodType());
        assertFalse(result.getIsCompleted().isValue());
        verify(animalService).getAnimalById(animalId);
        verify(scheduleRepository).save(any(FeedingSchedule.class));
    }

    @Test
    void getScheduleById_ExistingId_ReturnsSchedule() {
        Long scheduleId = 1L;
        FeedingSchedule expectedSchedule = new FeedingSchedule();
        expectedSchedule.setId(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(expectedSchedule));

        FeedingSchedule result = feedingScheduleService.getScheduleById(scheduleId);

        assertEquals(scheduleId, result.getId());
    }

    @Test
    void getScheduleById_NonExistingId_ThrowsException() {
        Long scheduleId = 999L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        assertThrows(FeedingScheduleNotFoundException.class, () ->
                feedingScheduleService.getScheduleById(scheduleId));
    }

    @Test
    void getAllSchedules_ReturnsAllSchedules() {
        List<FeedingSchedule> expectedSchedules = Arrays.asList(
                new FeedingSchedule(), new FeedingSchedule()
        );
        when(scheduleRepository.findAll()).thenReturn(expectedSchedules);

        List<FeedingSchedule> result = feedingScheduleService.getAllSchedules();

        assertEquals(2, result.size());
    }

    @Test
    void getUpcomingFeedings_ReturnsFutureFeedings() {
        LocalTime now = LocalTime.of(12, 0, 0, 0);
        FeedingSchedule futureSchedule = new FeedingSchedule();
        futureSchedule.setFeedingTime(new FeedingTime(LocalTime.of(14, 0)));

        when(scheduleRepository.findUpcomingFeedings(any(LocalTime.class))).thenReturn(List.of(futureSchedule));

        List<FeedingSchedule> result = feedingScheduleService.getUpcomingFeedings();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getFeedingTime().getValue().isAfter(now));
    }

    @Test
    void deleteSchedule_ExistingId_DeletesSuccessfully() {
        Long scheduleId = 1L;
        when(scheduleRepository.existsById(scheduleId)).thenReturn(true);

        feedingScheduleService.deleteSchedule(scheduleId);

        verify(scheduleRepository).deleteById(scheduleId);
    }

    @Test
    void deleteSchedule_NonExistingId_ThrowsException() {
        Long scheduleId = 999L;
        when(scheduleRepository.existsById(scheduleId)).thenReturn(false);

        assertThrows(FeedingScheduleNotFoundException.class, () ->
                feedingScheduleService.deleteSchedule(scheduleId));
        verify(scheduleRepository, never()).deleteById(any());
    }

    @Test
    void updateSchedule_ValidData_ReturnsUpdatedSchedule() {
        Long scheduleId = 1L;
        FeedingSchedule existingSchedule = new FeedingSchedule();
        existingSchedule.setId(scheduleId);
        existingSchedule.setFeedingTime(new FeedingTime(LocalTime.of(10, 0)));

        FeedingSchedule updatedDetails = new FeedingSchedule();
        updatedDetails.setFeedingTime(new FeedingTime(LocalTime.of(14, 0)));
        updatedDetails.setFoodType(FoodType.FISH);

        when(scheduleRepository.existsById(scheduleId)).thenReturn(true);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSchedule));
        when(scheduleRepository.save(any(FeedingSchedule.class))).thenReturn(existingSchedule);

        FeedingSchedule result = feedingScheduleService.updateSchedule(scheduleId, updatedDetails);

        assertEquals(LocalTime.of(14, 0), result.getFeedingTime().getValue());
        assertEquals(FoodType.FISH, result.getFoodType());
    }

    @Test
    void completeFeeding_ValidId_MarksAsCompleted() {
        Long scheduleId = 1L;
        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setIsCompleted(new FeedingScheduleCompletedFlag(false));

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(FeedingSchedule.class))).thenReturn(schedule);

        feedingScheduleService.completeFeeding(scheduleId);

        assertTrue(schedule.getIsCompleted().isValue());
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void completeFeeding_AlreadyCompleted_NoChanges() {
        Long scheduleId = 1L;
        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setIsCompleted(new FeedingScheduleCompletedFlag(true));

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        feedingScheduleService.completeFeeding(scheduleId);

        assertTrue(schedule.getIsCompleted().isValue());
        verify(scheduleRepository).save(schedule);
    }
}