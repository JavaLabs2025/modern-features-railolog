package org.lab.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;

public class MilestoneRepository {
    private final Map<Long, Milestone> milestones = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Milestone save(Milestone milestone) {
        if (milestone.getId() == null) {
            milestone.setId(idGenerator.getAndIncrement());
        }
        milestones.put(milestone.getId(), milestone);
        return milestone;
    }

    public Optional<Milestone> findById(Long id) {
        return Optional.ofNullable(milestones.get(id));
    }

    public List<Milestone> findByStatus(MilestoneStatus status) {
        return milestones.values().stream()
                .filter(milestone -> milestone.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Milestone> findAll() {
        return milestones.values().stream().collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return milestones.containsKey(id);
    }

    public void deleteById(Long id) {
        milestones.remove(id);
    }
}