package org.lab.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.lab.model.BugReport;
import org.lab.model.BugReportStatus;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.BugReportRepository;
import org.lab.repository.ProjectRepository;

@RequiredArgsConstructor
public class BugReportService {

    private final BugReportRepository bugReportRepository;
    private final ProjectRepository projectRepository;
    private final UserRoleValidationService userRoleValidationService;

    public BugReport findById(Long id) {
        return bugReportRepository.findById(id).orElse(null);
    }

    public Set<BugReport> findBugReportsToFix(User user) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getDevelopers().contains(user))
                .map(project -> bugReportRepository.findByProjectId(project.getId()))
                .flatMap(List::stream)
                .filter(bugReport -> BugReportStatus.NEW.equals(bugReport.getStatus()))
                .collect(Collectors.toSet());
    }

    public BugReport createBugReportForProject(User user, Long projectId) {
        userRoleValidationService.validateUserHasRoles(user, projectId, Role.DEVELOPER, Role.QA);

        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }

        BugReport bugReport = new BugReport();
        bugReport.setProjectId(projectId);
        bugReport.setStatus(BugReportStatus.NEW);

        return bugReport;
    }

    public BugReport fixBugReport(User developer, Long bugReportId) {
        BugReport bugReport = findById(bugReportId);
        if (bugReport == null) {
            throw new IllegalArgumentException("Bug report with ID '" + bugReportId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(developer, bugReport.getProjectId(), Role.DEVELOPER);

        if (!BugReportStatus.NEW.equals(bugReport.getStatus()) && !BugReportStatus.TESTED.equals(bugReport.getStatus())) {
            throw new IllegalStateException("Bug report cannot be fixed in current status: " + bugReport.getStatus());
        }

        bugReport.setStatus(BugReportStatus.FIXED);
        return bugReport;
    }

    public BugReport testProject(User tester, Long projectId) {
        userRoleValidationService.validateUserHasRoles(tester, projectId, Role.QA);

        return createBugReportForProject(tester, projectId);
    }

    public BugReport verifyBugFix(User tester, Long bugReportId, boolean isFixed) {
        BugReport bugReport = findById(bugReportId);
        if (bugReport == null) {
            throw new IllegalArgumentException("Bug report with ID '" + bugReportId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(tester, bugReport.getProjectId(), Role.QA);

        if (!BugReportStatus.FIXED.equals(bugReport.getStatus())) {
            throw new IllegalStateException("Bug report must be in FIXED status to verify. Current status: " + bugReport.getStatus());
        }

        if (isFixed) {
            bugReport.setStatus(BugReportStatus.TESTED);
        } else {
            bugReport.setStatus(BugReportStatus.NEW);
        }

        return bugReport;
    }

    public BugReport closeBugReport(User tester, Long bugReportId) {
        BugReport bugReport = findById(bugReportId);
        if (bugReport == null) {
            throw new IllegalArgumentException("Bug report with ID '" + bugReportId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(tester, bugReport.getProjectId(), Role.MANAGER);

        if (!BugReportStatus.TESTED.equals(bugReport.getStatus())) {
            throw new IllegalStateException("Bug report must be tested before closing. Current status: " + bugReport.getStatus());
        }

        bugReport.setStatus(BugReportStatus.CLOSED);
        return bugReport;
    }
}