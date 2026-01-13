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
    System.out.println("Registered manager: " + manager);
    
    User developer1 = userService.registerUser("developer1", "d");
    System.out.println("Registered developer1: " + developer1);
    
    User developer2 = userService.registerUser("developer2", "d");
    System.out.println("Registered developer2: " + developer2);
    
    User tester = userService.registerUser("tester", "t");
    System.out.println("Registered tester: " + tester);
    
    User teamLead = userService.registerUser("teamLead", "t");
    System.out.println("Registered teamLead: " + teamLead);

    Project project = projectService.createProject(manager);
    System.out.println("Created project: " + project);

    projectService.assignTeamLeader(manager, project.getId(), teamLead);
    System.out.println("Assigned team leader to project");
    
    projectService.addDeveloperToProject(manager, project.getId(), developer1);
    System.out.println("Added developer1 to project");
    
    projectService.addDeveloperToProject(manager, project.getId(), developer2);
    System.out.println("Added developer2 to project");
    
    projectService.addTesterToProject(manager, project.getId(), tester);
    System.out.println("Added tester to project");

    Milestone milestone = milestoneService.createMilestone(
            manager,
            LocalDate.now(),
            LocalDate.now().plusDays(2),
            project
    );
    System.out.println("Created milestone: " + milestone);

    milestoneService.changeMilestoneStatus(manager, project.getId(), milestone.getId(), MilestoneStatus.ACTIVE);
    System.out.println("Changed milestone status to ACTIVE " + milestone);

    Ticket ticket = ticketService.createTicketForProject(teamLead, milestone.getId());
    System.out.println("Created ticket: " + ticket);

    ticketService.assignDeveloperToTicket(manager, ticket.getId(), developer2);
    System.out.println("Assigned developer2 to ticket");

    ticketService.executeTicket(developer2, ticket.getId());
    System.out.println("Executed ticket (first time)");
    
    ticketService.executeTicket(developer2, ticket.getId());
    System.out.println("Executed ticket (second time)");

    boolean ticketCompletion = ticketService.checkTicketCompletion(teamLead, ticket.getId());
    System.out.println("Ticket completion status: " + ticketCompletion);

    BugReport bugReport = bugReportService.testProject(tester, project.getId());
    System.out.println("Created bug report from testing: " + bugReport);

    Set<BugReport> fixedBugReports = bugReportService.findBugReportsToFix(developer1).stream()
            .map(bug -> bugReportService.fixBugReport(developer1, bug.getId()))
            .collect(Collectors.toSet());
    System.out.println("Fixed bug reports: " + fixedBugReports);

    Stream<BugReport> verifiedReports = fixedBugReports.stream()
            .map(report -> bugReportService.verifyBugFix(tester, report.getId(), true));
    System.out.println("Verifying bug reports...");

    verifiedReports.forEach(report -> {
        bugReportService.closeBugReport(manager, report.getId());
        System.out.println("Closed bug report: " + report);
    });
}