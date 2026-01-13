package org.lab.model;

public sealed interface Task permits Ticket, BugReport {
    Long getId();
}
