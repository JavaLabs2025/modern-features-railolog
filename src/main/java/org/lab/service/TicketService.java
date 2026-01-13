package org.lab.service;

import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import org.lab.model.Milestone;
import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.model.User;
import org.lab.repository.TicketRepository;

@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectService projectService;
    private final MilestoneService milestoneService;
    private final UserRoleValidationService userRoleValidationService;

    public Ticket findById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    private Long getProjectIdFromMilestone(Long milestoneId) {
        Milestone milestone = milestoneService.findById(milestoneId);
        if (milestone == null) {
            throw new IllegalArgumentException("Milestone with ID '" + milestoneId + "' does not exist");
        }

        return projectService.findAll().stream()
                .filter(project -> project.getMilestones().contains(milestone))
                .findFirst()
                .map(Project::getId)
                .orElseThrow(() -> new IllegalArgumentException("Milestone with ID '" + milestoneId + "' does not " +
                        "exist in any project"));
    }

    public Ticket createTicketForProject(User user, Long milestoneId) {
        Long projectId = getProjectIdFromMilestone(milestoneId);
        userRoleValidationService.validateUserHasRoles(user, projectId, Role.MANAGER, Role.TEAMLEAD);

        Ticket ticket = new Ticket();
        ticket.setMilestoneId(milestoneId);
        ticket.setStatus(TicketStatus.NEW);
        ticket.setAssignees(new HashSet<>());

        Ticket savedTicket = ticketRepository.save(ticket);

        Milestone milestone = milestoneService.findById(milestoneId);
        if (milestone.getTickets() == null) {
            milestone.setTickets(new HashSet<>());
        }
        milestone.getTickets().add(savedTicket);

        return savedTicket;
    }

    public Ticket assignDeveloperToTicket(User user, Long ticketId, User developer) {
        Ticket ticket = findById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket with ID '" + ticketId + "' does not exist");
        }

        Long projectId = getProjectIdFromMilestone(ticket.getMilestoneId());
        userRoleValidationService.validateUserHasRoles(user, projectId, Role.MANAGER, Role.TEAMLEAD);

        Project project = projectService.findById(projectId);
        if (!project.getDevelopers().contains(developer)) {
            throw new IllegalArgumentException("User is not a developer in this project");
        }

        ticket.getAssignees().add(developer);
        return ticketRepository.save(ticket);
    }

    public boolean checkTicketCompletion(User user, Long ticketId) {
        Ticket ticket = findById(ticketId);

        Long projectId = getProjectIdFromMilestone(ticket.getMilestoneId());
        userRoleValidationService.validateUserHasRoles(user, projectId, Role.MANAGER, Role.TEAMLEAD);

        return TicketStatus.COMPLETED.equals(ticket.getStatus());
    }

    public Ticket executeTicket(User developer, Long ticketId) {
        Ticket ticket = findById(ticketId);
        Long projectId = getProjectIdFromMilestone(ticket.getMilestoneId());
        userRoleValidationService.validateUserHasRoles(developer, projectId, Role.DEVELOPER);

        if (!ticket.getAssignees().contains(developer)) {
            throw new SecurityException("Developer is not assigned to this ticket");
        }

        if (TicketStatus.NEW.equals(ticket.getStatus()) || TicketStatus.ACCEPTED.equals(ticket.getStatus())) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        } else if (TicketStatus.IN_PROGRESS.equals(ticket.getStatus())) {
            ticket.setStatus(TicketStatus.COMPLETED);
        }

        return ticket;
    }
}