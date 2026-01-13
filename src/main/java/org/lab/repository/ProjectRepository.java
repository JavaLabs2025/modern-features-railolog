package org.lab.repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.model.Project;
import org.lab.model.User;

public class ProjectRepository {
    private final Map<Long, Project> projects = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId(idGenerator.getAndIncrement());
        }
        projects.put(project.getId(), project);
        return project;
    }

    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    public List<Project> findByManager(User manager) {
        return projects.values().stream()
                .filter(project -> project.getManager().equals(manager))
                .collect(Collectors.toList());
    }

    public List<Project> findByTeamLeader(User teamLeader) {
        return projects.values().stream()
                .filter(project -> project.getTeamLeader().equals(teamLeader))
                .collect(Collectors.toList());
    }

    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }

    public boolean existsById(Long id) {
        return projects.containsKey(id);
    }

    public void deleteById(Long id) {
        projects.remove(id);
    }
}