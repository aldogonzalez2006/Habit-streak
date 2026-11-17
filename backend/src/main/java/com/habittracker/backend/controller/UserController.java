package com.habittracker.backend.controller;

import com.habittracker.backend.dto.CreateUserRequest;
import com.habittracker.backend.dto.UserResponse;
import com.habittracker.backend.entity.User;
import com.habittracker.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody CreateUserRequest request) {

        User user = userService.createUser(request);

        UserResponse response = new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getCreatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}