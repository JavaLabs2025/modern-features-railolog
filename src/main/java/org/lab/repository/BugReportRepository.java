package org.lab.repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.model.BugReport;

public class BugReportRepository {
    private final Map<Long, BugReport> bugReports = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public BugReport save(BugReport bugReport) {
        if (bugReport.getId() == null) {
            bugReport.setId(idGenerator.getAndIncrement());
        }
        bugReports.put(bugReport.getId(), bugReport);
        return bugReport;
    }

    public Optional<BugReport> findById(Long id) {
        return Optional.ofNullable(bugReports.get(id));
    }

    public List<BugReport> findByProjectId(Long projectId) {
        return bugReports.values().stream()
                .filter(bugReport -> Objects.equals(bugReport.getProjectId(), projectId))
                .collect(Collectors.toList());
    }

    public List<BugReport> findAll() {
        return bugReports.values().stream().collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return bugReports.containsKey(id);
    }

    public void deleteById(Long id) {
        bugReports.remove(id);
    }
}