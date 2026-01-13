package org.lab.model;

import java.util.Set;

public class Project {
    private long id;
    private Set<User> developers;
    private Set<User> testers;
    private User manager;
    private User teamLeader;
    private Set<Milestone> milestones;
}
