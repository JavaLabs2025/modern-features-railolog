package org.lab.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticket {
    private Long id;
    private long milestoneId;
    private TicketStatus status;
    private Set<User> assignees;
}
