package org.lab.model;

import java.util.Set;

public class Ticket {
    private long id;
    private long milestoneId;
    private TicketStatus status;
    private Set<User> assignees;
}
