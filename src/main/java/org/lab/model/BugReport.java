package org.lab.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class BugReport implements Task {
    private Long id;
    private long projectId;
    private BugReportStatus status;
}
