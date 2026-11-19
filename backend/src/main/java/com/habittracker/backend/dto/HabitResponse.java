
package com.habittracker.backend.dto;

import java.time.OffsetDateTime;

public record HabitResponse(
    Long id,
    Long userId,
    String name,
    String description,
    String color,
    Boolean isActive,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
}
