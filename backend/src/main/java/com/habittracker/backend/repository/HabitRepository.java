
package com.habittracker.backend.repository;

import com.habittracker.backend.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ¿Por qué Optional?
 * Porque una búsqueda puede no encontrar ningún resultado. Optional nos permite representar explícitamente esa posibilidad sin devolver null.
 */

public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findAllByUser_Id(Long userId);

    Optional<Habit> findByIdAndUser_Id(Long habitId, Long userId);

}
