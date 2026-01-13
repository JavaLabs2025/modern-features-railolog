package org.lab.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BugReport {
    private Long id;
    private long projectId;
    private BugReportStatus status;
}
