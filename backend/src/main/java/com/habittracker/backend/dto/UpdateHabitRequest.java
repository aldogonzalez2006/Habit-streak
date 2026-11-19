
package com.habittracker.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateHabitRequest(

    @NotBlank(message = "El nombre del hábito es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    String name,

    String description,

    @Size(max = 20, message = "El color no puede superar los 20 caracteres.")
    String color

) {
}
