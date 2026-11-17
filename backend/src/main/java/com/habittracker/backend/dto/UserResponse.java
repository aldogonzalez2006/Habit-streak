package com.habittracker.backend.dto;
// CreateUserRequest: representa lo que recibimos.
// UserResponse: representa lo que devolvemos.
import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    LocalDateTime createdAt
) {
}