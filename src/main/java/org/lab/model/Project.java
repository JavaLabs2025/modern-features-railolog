package org.lab.model;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Project {
    private Long id;
    private Set<User> developers;
    private Set<User> testers;
    private User manager;
    private User teamLeader;
    private Set<Milestone> milestones;
}
