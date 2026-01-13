package org.lab.service;

import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.repository.MilestoneRepository;
import java.time.LocalDate;
import java.util.List;

public class MilestoneService {
    
    private final MilestoneRepository milestoneRepository;

    public MilestoneService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    public Milestone createMilestone(MilestoneStatus status, LocalDate startDate, LocalDate endDate) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Milestone milestone = new Milestone();
        milestone.setStatus(status);
        milestone.setStartDate(startDate);
        milestone.setEndDate(endDate);

        return milestoneRepository.save(milestone);
    }

    public Milestone findById(Long id) {
        return milestoneRepository.findById(id).orElse(null);
    }

    public List<Milestone> findByStatus(MilestoneStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        return milestoneRepository.findByStatus(status);
    }

    public List<Milestone> findAll() {
        return milestoneRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!milestoneRepository.existsById(id)) {
            throw new IllegalArgumentException("Milestone with ID '" + id + "' does not exist");
        }
        milestoneRepository.deleteById(id);
    }
}