package org.lab.service;

import org.lab.model.BugReport;
import org.lab.repository.BugReportRepository;
import java.util.List;

public class BugReportService {
    
    private final BugReportRepository bugReportRepository;

    public BugReportService(BugReportRepository bugReportRepository) {
        this.bugReportRepository = bugReportRepository;
    }

    public BugReport createBugReport(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }

        BugReport bugReport = new BugReport();
        bugReport.setProjectId(projectId);

        return bugReportRepository.save(bugReport);
    }

    public BugReport findById(Long id) {
        return bugReportRepository.findById(id).orElse(null);
    }

    public List<BugReport> findByProjectId(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return bugReportRepository.findByProjectId(projectId);
    }

    public List<BugReport> findAll() {
        return bugReportRepository.findAll();
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!bugReportRepository.existsById(id)) {
            throw new IllegalArgumentException("BugReport with ID '" + id + "' does not exist");
        }
        bugReportRepository.deleteById(id);
    }
}