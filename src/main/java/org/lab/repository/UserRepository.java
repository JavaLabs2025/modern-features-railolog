package org.lab.repository;

import org.lab.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByLogin(String login) {
        return users.values().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    public boolean existsByLogin(String login) {
        return users.values().stream()
                .anyMatch(user -> user.getLogin().equals(login));
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    public Map<Long, User> findAll() {
        return new HashMap<>(users);
    }
}