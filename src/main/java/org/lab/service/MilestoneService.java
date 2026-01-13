package org.lab.service;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.MilestoneRepository;

@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectService projectService;
    private final UserRoleValidationService userRoleValidationService;

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

    public Milestone createMilestoneForProject(
            User manager,
            Long projectId,
            MilestoneStatus status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        userRoleValidationService.validateUserHasRoles(manager, projectId, Role.MANAGER);
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Milestone milestone = new Milestone();
        milestone.setStatus(status);
        milestone.setStartDate(startDate);
        milestone.setEndDate(endDate);

        Milestone savedMilestone = milestoneRepository.save(milestone);

        Project project = projectService.findById(projectId);
        project.getMilestones().add(savedMilestone);

        return savedMilestone;
    }

    public Milestone changeMilestoneStatus(User manager, Long projectId, Long milestoneId, MilestoneStatus newStatus) {
        userRoleValidationService.validateUserHasRoles(manager, projectId, Role.MANAGER);

        Milestone milestone = findById(milestoneId);
        if (milestone == null) {
            throw new IllegalArgumentException("Milestone with ID '" + milestoneId + "' does not exist");
        }

        milestone.setStatus(newStatus);
        return milestone;
    }
}