package org.lab.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class Ticket implements Task {
    private Long id;
    private long milestoneId;
    private TicketStatus status;
    private Set<User> assignees;
}
