package org.lab.model;

import java.time.LocalDate;
import java.util.Set;

public class Milestone {
    private MilestoneStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Ticket> tickets;
}
