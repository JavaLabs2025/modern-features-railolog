package org.lab.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.repository.ProjectRepository;

@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRoleValidationService userRoleValidationService;

    public Project createProject(User manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }

        Project project = new Project();
        project.setManager(manager);
        project.setDevelopers(new HashSet<>());
        project.setTesters(new HashSet<>());
        project.setMilestones(new HashSet<>());

        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Set<Project> findInvolvedProjects(User user) {
        return projectRepository.findAll().stream()
                .filter(project -> isMember(user, project))
                .collect(Collectors.toSet());
    }

    public List<Project> findByManager(User manager) {
        return projectRepository.findByManager(manager);
    }

    public List<Project> findByTeamLeader(User teamLeader) {
        return projectRepository.findByTeamLeader(teamLeader);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project addDeveloper(Long projectId, User developer) {
        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        project.getDevelopers().add(developer);
        return projectRepository.save(project);
    }

    public Project addTester(Long projectId, User tester) {
        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        project.getTesters().add(tester);
        return projectRepository.save(project);
    }

    public void deleteById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project with ID '" + id + "' does not exist");
        }
        projectRepository.deleteById(id);
    }

    public Project assignTeamLeader(User manager, Long projectId, User teamLeader) {
        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(manager, project.getId(), Role.MANAGER);

        project.setTeamLeader(teamLeader);
        return project;
    }

    public Project addDeveloperToProject(User manager, Long projectId, User developer) {
        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(manager, project.getId(), Role.MANAGER);

        project.getDevelopers().add(developer);
        return project;
    }

    public Project addTesterToProject(User manager, Long projectId, User tester) {
        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        userRoleValidationService.validateUserHasRoles(manager, project.getId(), Role.MANAGER);

        project.getTesters().add(tester);
        return project;
    }

    private boolean isMember(User user, Project project) {
        return project.getManager() == user ||
                project.getTeamLeader() == user ||
                project.getDevelopers().contains(user) ||
                project.getTesters().contains(user);
    }
}