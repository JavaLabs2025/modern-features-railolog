package org.lab.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Milestone {
    private Long id;
    private MilestoneStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Set<Ticket> tickets;
}
