package org.lab.service;

import org.lab.model.User;
import org.lab.repository.UserRepository;

public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists");
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        User user = new User();
        user.setLogin(login.trim());
        user.setPassword(password);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}