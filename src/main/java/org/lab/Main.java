import org.lab.ManagementSystem;
import org.lab.model.BugReport;
import org.lab.model.Milestone;
import org.lab.model.MilestoneStatus;
import org.lab.model.Project;
import org.lab.model.Ticket;
import org.lab.model.User;
import org.lab.service.BugReportService;
import org.lab.service.MilestoneService;
import org.lab.service.ProjectService;
import org.lab.service.TicketService;
import org.lab.service.UserService;

void main() {
    ManagementSystem system = ManagementSystem.defaults();

    UserService userService = system.userService();
    ProjectService projectService = system.projectService();
    BugReportService bugReportService = system.bugReportService();
    MilestoneService milestoneService = system.milestoneService();
    TicketService ticketService = system.ticketService();

    User manager = userService.registerUser("manager", "m");
    User developer1 = userService.registerUser("developer1", "d");
    User developer2 = userService.registerUser("developer2", "d");
    User tester = userService.registerUser("tester", "t");
    User teamLead = userService.registerUser("teamLead", "t");

    Project project = projectService.createProject(manager);

    projectService.assignTeamLeader(manager, project.getId(), teamLead);
    projectService.addDeveloperToProject(manager, project.getId(), developer1);
    projectService.addDeveloperToProject(manager, project.getId(), developer2);
    projectService.addTesterToProject(manager, project.getId(), tester);

    Milestone milestone = milestoneService.createMilestone(
            manager,
            LocalDate.now(),
            LocalDate.now().plusDays(2),
            project
    );

    milestoneService.changeMilestoneStatus(manager, project.getId(), milestone.getId(), MilestoneStatus.ACTIVE);

    Ticket ticket = ticketService.createTicketForProject(teamLead, milestone.getId());

    ticketService.assignDeveloperToTicket(manager, ticket.getId(), developer2);

    ticketService.executeTicket(developer2, ticket.getId());
    ticketService.executeTicket(developer2, ticket.getId());

    boolean ticketCompletion = ticketService.checkTicketCompletion(teamLead, ticket.getId());

    BugReport bugReport = bugReportService.testProject(tester, project.getId());

    Set<BugReport> fixedBugReports = bugReportService.findBugReportsToFix(developer1).stream()
            .map(bug -> bugReportService.fixBugReport(developer1, bug.getId()))
            .collect(Collectors.toSet());

    Stream<BugReport> verifiedReports = fixedBugReports.stream()
            .map(report -> bugReportService.verifyBugFix(tester, report.getId(), true));

    verifiedReports.forEach(report -> bugReportService.closeBugReport(manager, report.getId()));
}