package org.lab.service;

import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String login, String password, Role role) {
        if (userRepository.existsByLogin(login)) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists");
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (role == null) {
            role = Role.USER; // Default role
        }

        User user = new User();
        user.setLogin(login.trim());
        user.setPassword(password); // In real application, password should be hashed
        user.setRole(role);

        return userRepository.save(user);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}