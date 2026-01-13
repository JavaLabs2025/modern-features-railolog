package org.lab;

import org.lab.repository.BugReportRepository;
import org.lab.repository.MilestoneRepository;
import org.lab.repository.ProjectRepository;
import org.lab.repository.TicketRepository;
import org.lab.repository.UserRepository;
import org.lab.service.BugReportService;
import org.lab.service.MilestoneService;
import org.lab.service.ProjectService;
import org.lab.service.TicketService;
import org.lab.service.UserRoleValidationService;
import org.lab.service.UserService;

public record ManagementSystem(
        BugReportService bugReportService,
        MilestoneService milestoneService,
        ProjectService projectService,
        TicketService ticketService,
        UserService userService,
        UserRoleValidationService userRoleValidationService
) {
    public static ManagementSystem defaults() {
        BugReportRepository bugReportRepository = new BugReportRepository();
        MilestoneRepository milestoneRepository = new MilestoneRepository();
        ProjectRepository projectRepository = new ProjectRepository();
        TicketRepository ticketRepository = new TicketRepository();
        UserRepository userRepository = new UserRepository();

        UserRoleValidationService userRoleValidationService = new UserRoleValidationService(projectRepository);

        ProjectService projectService = new ProjectService(
                projectRepository,
                userRoleValidationService
        );
        MilestoneService milestoneService = new MilestoneService(
                milestoneRepository,
                projectService,
                ticketRepository,
                userRoleValidationService
        );
        return new ManagementSystem(
                new BugReportService(
                        bugReportRepository,
                        projectRepository,
                        userRoleValidationService
                ),
                milestoneService,
                projectService,
                new TicketService(
                        ticketRepository,
                        projectService,
                        milestoneService,
                        userRoleValidationService
                ),
                new UserService(
                        userRepository,
                        projectRepository,
                        ticketRepository
                ),
                userRoleValidationService
        );
    }
}
