package com.habittracker.backend.service;

import com.habittracker.backend.dto.CreateUserRequest;
import com.habittracker.backend.entity.User;
import com.habittracker.backend.exception.EmailAlreadyExistsException;
import com.habittracker.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest request) {

        String email = request.email().trim();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException(
                "El correo electrónico ya está registrado."
            );
        }

        User user = new User(email);

        return userRepository.save(user);
    }
}