package org.lab.service;

import org.lab.model.Project;
import org.lab.model.User;
import org.lab.repository.ProjectRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ProjectService {
    
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(User manager, User teamLeader) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }

        if (teamLeader == null) {
            throw new IllegalArgumentException("Team leader cannot be null");
        }

        Project project = new Project();
        project.setManager(manager);
        project.setTeamLeader(teamLeader);
        project.setDevelopers(new HashSet<>());
        project.setTesters(new HashSet<>());
        project.setMilestones(new HashSet<>());

        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> findByManager(User manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }
        return projectRepository.findByManager(manager);
    }

    public List<Project> findByTeamLeader(User teamLeader) {
        if (teamLeader == null) {
            throw new IllegalArgumentException("Team leader cannot be null");
        }
        return projectRepository.findByTeamLeader(teamLeader);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project addDeveloper(Long projectId, User developer) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        if (developer == null) {
            throw new IllegalArgumentException("Developer cannot be null");
        }

        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        project.getDevelopers().add(developer);
        return projectRepository.save(project);
    }

    public Project addTester(Long projectId, User tester) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        if (tester == null) {
            throw new IllegalArgumentException("Tester cannot be null");
        }

        Project project = findById(projectId);
        if (project == null) {
            throw new IllegalArgumentException("Project with ID '" + projectId + "' does not exist");
        }

        project.getTesters().add(tester);
        return projectRepository.save(project);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project with ID '" + id + "' does not exist");
        }
        projectRepository.deleteById(id);
    }
}