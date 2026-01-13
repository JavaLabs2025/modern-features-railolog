package org.lab.service;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.lab.model.Ticket;
import org.lab.model.User;
import org.lab.repository.ProjectRepository;
import org.lab.repository.TicketRepository;
import org.lab.repository.UserRepository;

@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;

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

    public Set<Ticket> findTasks(User user) {
        return ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getAssignees().contains(user))
                .collect(Collectors.toSet());
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}