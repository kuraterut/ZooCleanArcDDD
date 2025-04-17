package org.kuraterut.zoohm2hse.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuraterut.zoohm2hse.application.exceptions.FeedingScheduleNotFoundException;
import org.kuraterut.zoohm2hse.application.services.AnimalService;
import org.kuraterut.zoohm2hse.application.services.FeedingScheduleService;
import org.kuraterut.zoohm2hse.domain.Animal;
import org.kuraterut.zoohm2hse.domain.FeedingSchedule;
import org.kuraterut.zoohm2hse.domain.valueobjects.animal.FoodType;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingScheduleCompletedFlag;
import org.kuraterut.zoohm2hse.domain.valueobjects.feedingSchedule.FeedingTime;
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

    // 1. Тест для createSchedule()
    @Test
    void createSchedule_ValidRequest_ReturnsSavedSchedule() {
        // Arrange
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

        // Act
        FeedingSchedule result = feedingScheduleService.createSchedule(request);

        // Assert
        assertNotNull(result);
        assertEquals(animalId, result.getAnimal().getId());
        assertEquals(feedingTime, result.getFeedingTime().getValue());
        assertEquals(FoodType.MEAT, result.getFoodType());
        assertFalse(result.getIsCompleted().isValue());
        verify(animalService).getAnimalById(animalId);
        verify(scheduleRepository).save(any(FeedingSchedule.class));
    }

    // 2. Тест для getScheduleById()
    @Test
    void getScheduleById_ExistingId_ReturnsSchedule() {
        // Arrange
        Long scheduleId = 1L;
        FeedingSchedule expectedSchedule = new FeedingSchedule();
        expectedSchedule.setId(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(expectedSchedule));

        // Act
        FeedingSchedule result = feedingScheduleService.getScheduleById(scheduleId);

        // Assert
        assertEquals(scheduleId, result.getId());
    }

    @Test
    void getScheduleById_NonExistingId_ThrowsException() {
        // Arrange
        Long scheduleId = 999L;
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FeedingScheduleNotFoundException.class, () ->
                feedingScheduleService.getScheduleById(scheduleId));
    }

    // 3. Тест для getAllSchedules()
    @Test
    void getAllSchedules_ReturnsAllSchedules() {
        // Arrange
        List<FeedingSchedule> expectedSchedules = Arrays.asList(
                new FeedingSchedule(), new FeedingSchedule()
        );
        when(scheduleRepository.findAll()).thenReturn(expectedSchedules);

        // Act
        List<FeedingSchedule> result = feedingScheduleService.getAllSchedules();

        // Assert
        assertEquals(2, result.size());
    }

    // 4. Тест для getUpcomingFeedings()
    @Test
    void getUpcomingFeedings_ReturnsFutureFeedings() {
        // Arrange
        LocalTime now = LocalTime.of(12, 0, 0, 0);
        FeedingSchedule futureSchedule = new FeedingSchedule();
        futureSchedule.setFeedingTime(new FeedingTime(LocalTime.of(14, 0)));

        when(scheduleRepository.findUpcomingFeedings(any(LocalTime.class))).thenReturn(List.of(futureSchedule));

        // Act
        List<FeedingSchedule> result = feedingScheduleService.getUpcomingFeedings();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getFeedingTime().getValue().isAfter(now));
    }

    // 5. Тест для deleteSchedule()
    @Test
    void deleteSchedule_ExistingId_DeletesSuccessfully() {
        // Arrange
        Long scheduleId = 1L;
        when(scheduleRepository.existsById(scheduleId)).thenReturn(true);

        // Act
        feedingScheduleService.deleteSchedule(scheduleId);

        // Assert
        verify(scheduleRepository).deleteById(scheduleId);
    }

    @Test
    void deleteSchedule_NonExistingId_ThrowsException() {
        // Arrange
        Long scheduleId = 999L;
        when(scheduleRepository.existsById(scheduleId)).thenReturn(false);

        // Act & Assert
        assertThrows(FeedingScheduleNotFoundException.class, () ->
                feedingScheduleService.deleteSchedule(scheduleId));
        verify(scheduleRepository, never()).deleteById(any());
    }

    // 6. Тест для updateSchedule()
    @Test
    void updateSchedule_ValidData_ReturnsUpdatedSchedule() {
        // Arrange
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

        // Act
        FeedingSchedule result = feedingScheduleService.updateSchedule(scheduleId, updatedDetails);

        // Assert
        assertEquals(LocalTime.of(14, 0), result.getFeedingTime().getValue());
        assertEquals(FoodType.FISH, result.getFoodType());
    }

    // 7. Тест для completeFeeding()
    @Test
    void completeFeeding_ValidId_MarksAsCompleted() {
        // Arrange
        Long scheduleId = 1L;
        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setIsCompleted(new FeedingScheduleCompletedFlag(false));

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(FeedingSchedule.class))).thenReturn(schedule);

        // Act
        feedingScheduleService.completeFeeding(scheduleId);

        // Assert
        assertTrue(schedule.getIsCompleted().isValue());
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void completeFeeding_AlreadyCompleted_NoChanges() {
        // Arrange
        Long scheduleId = 1L;
        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setIsCompleted(new FeedingScheduleCompletedFlag(true));

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        // Act
        feedingScheduleService.completeFeeding(scheduleId);

        // Assert
        assertTrue(schedule.getIsCompleted().isValue());
        verify(scheduleRepository).save(schedule); // Все равно сохраняем
    }
}