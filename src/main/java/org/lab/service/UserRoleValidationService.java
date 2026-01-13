package org.lab.service;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.ProjectRepository;

@RequiredArgsConstructor
public class UserRoleValidationService {
    private final ProjectRepository projectRepository;

    public void validateUserHasRoles(User user, Long projectId, Role... roles) {
        Project project = projectRepository.findById(projectId).orElseThrow(() ->
                new IllegalArgumentException("Project with ID '" + projectId + "' does not exist")
        );
        boolean anyMatch = Arrays.stream(roles).anyMatch(role -> role.equals(getUserRole(user, project)));
        if (!anyMatch) {
            throw new SecurityException("User does not have required roles");
        }
    }

    public Role getUserRole(User user, Project project) {
        if (project.getManager() != null && project.getManager().equals(user)) {
            return Role.MANAGER;
        }
        if (project.getTeamLeader() != null && project.getTeamLeader().equals(user)) {
            return Role.TEAMLEAD;
        }
        if (project.getDevelopers().contains(user)) {
            return Role.DEVELOPER;
        }
        if (project.getTesters().contains(user)) {
            return Role.QA;
        }
        return null;
    }


}
