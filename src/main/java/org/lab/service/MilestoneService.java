package org.lab.service;

import java.time.LocalDate;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.TicketStatus;
import org.lab.model.User;
import org.lab.repository.MilestoneRepository;
import org.lab.repository.TicketRepository;

@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectService projectService;
    private final TicketRepository ticketRepository;
    private final UserRoleValidationService userRoleValidationService;

    public Milestone createMilestone(User manager, LocalDate startDate, LocalDate endDate, Project project) {
        userRoleValidationService.validateUserHasRoles(manager, project.getId(), Role.MANAGER);

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        Milestone milestone = new Milestone();
        milestone.setStatus(MilestoneStatus.OPEN);
        milestone.setStartDate(startDate);
        milestone.setEndDate(endDate);

        project.getMilestones().add(milestone);

        return milestoneRepository.save(milestone);
    }

    public Milestone findById(Long id) {
        return milestoneRepository.findById(id).orElse(null);
    }

    public Milestone changeMilestoneStatus(User manager, Long projectId, Long milestoneId, MilestoneStatus newStatus) {
        userRoleValidationService.validateUserHasRoles(manager, projectId, Role.MANAGER);

        Milestone milestone = findById(milestoneId);
        if (newStatus == MilestoneStatus.CLOSED) {
            validateTicketsClosed(milestone);
        }

        milestone.setStatus(newStatus);
        return milestone;
    }

    private void validateTicketsClosed(Milestone milestone) {
        milestone.getTickets().stream()
                .filter(ticket -> Objects.equals(ticket.getMilestoneId(), milestone.getId()))
                .forEach(ticket -> {
                    if (!ticket.getStatus().equals(TicketStatus.COMPLETED)) {
                        throw new IllegalArgumentException("Ticket with ID '" + ticket.getId() + "' is not completed");
                    }
                });
    }
}