
package com.habittracker.backend.service;

import com.habittracker.backend.dto.CreateHabitRequest;
import com.habittracker.backend.dto.UpdateHabitRequest;
import com.habittracker.backend.entity.Habit;
import com.habittracker.backend.entity.User;
import com.habittracker.backend.exception.ResourceNotFoundException;
import com.habittracker.backend.repository.HabitRepository;
import com.habittracker.backend.repository.UserRepository;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public HabitService(
            HabitRepository habitRepository,
            UserRepository userRepository,
            EntityManager entityManager) {

        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    // Crear un hábito
    @Transactional
    public Habit createHabit(Long userId, CreateHabitRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El usuario no existe."
                ));

        Habit habit = new Habit(
                user,
                request.name().trim(),
                request.description(),
                request.color()
        );

        Habit savedHabit = habitRepository.saveAndFlush(habit);

        entityManager.refresh(savedHabit);

        return savedHabit;
    }

    // Listar hábitos de un usuario
    @Transactional(readOnly = true)
    public List<Habit> getHabitsByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "El usuario no existe."
            );
        }

        return habitRepository.findAllByUser_Id(userId);
    }

    // Consultar un hábito específico
    @Transactional(readOnly = true)
    public Habit getHabitById(Long userId, Long habitId) {

        return habitRepository.findByIdAndUser_Id(habitId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El hábito no existe para este usuario."
                ));
    }

    // Actualizar un hábito
    @Transactional
    public Habit updateHabit(
            Long userId,
            Long habitId,
            UpdateHabitRequest request) {

        Habit habit = getHabitById(userId, habitId);

        habit.updateDetails(
                request.name().trim(),
                request.description(),
                request.color()
        );

        entityManager.flush();
        entityManager.refresh(habit);

        return habit;
    }

    // Archivar un hábito
    @Transactional
    public Habit archiveHabit(Long userId, Long habitId) {

        Habit habit = getHabitById(userId, habitId);

        habit.archive();

        entityManager.flush();
        entityManager.refresh(habit);

        return habit;
    }
}
