
package com.habittracker.backend.controller;

import com.habittracker.backend.dto.CreateHabitRequest;
import com.habittracker.backend.dto.HabitResponse;
import com.habittracker.backend.dto.UpdateHabitRequest;
import com.habittracker.backend.entity.Habit;
import com.habittracker.backend.service.HabitService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    // Crear un hábito
    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(
            @PathVariable Long userId,
            @Valid @RequestBody CreateHabitRequest request) {

        Habit habit = habitService.createHabit(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(habit));
    }

    // Listar hábitos de un usuario
    @GetMapping
    public ResponseEntity<List<HabitResponse>> getHabits(
            @PathVariable Long userId) {

        List<HabitResponse> habits = habitService.getHabitsByUser(userId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(habits);
    }

    // Consultar un hábito específico
    @GetMapping("/{habitId}")
    public ResponseEntity<HabitResponse> getHabitById(
            @PathVariable Long userId,
            @PathVariable Long habitId) {

        Habit habit = habitService.getHabitById(userId, habitId);

        return ResponseEntity.ok(toResponse(habit));
    }

    // Actualizar un hábito
    @PutMapping("/{habitId}")
    public ResponseEntity<HabitResponse> updateHabit(
            @PathVariable Long userId,
            @PathVariable Long habitId,
            @Valid @RequestBody UpdateHabitRequest request) {

        Habit habit = habitService.updateHabit(userId, habitId, request);

        return ResponseEntity.ok(toResponse(habit));
    }

    // Archivar un hábito
    @PatchMapping("/{habitId}/archive")
    public ResponseEntity<HabitResponse> archiveHabit(
            @PathVariable Long userId,
            @PathVariable Long habitId) {

        Habit habit = habitService.archiveHabit(userId, habitId);

        return ResponseEntity.ok(toResponse(habit));
    }

    // Convertir entidad a DTO de respuesta
    private HabitResponse toResponse(Habit habit) {
        return new HabitResponse(
                habit.getId(),
                habit.getUser().getId(),
                habit.getName(),
                habit.getDescription(),
                habit.getColor(),
                habit.getIsActive(),
                habit.getCreatedAt(),
                habit.getUpdatedAt()
        );
    }
}
