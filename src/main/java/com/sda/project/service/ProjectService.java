package com.sda.project.service;

import com.sda.project.controller.exception.ResourceAlreadyExistsException;
import com.sda.project.controller.exception.ResourceNotFoundException;
import com.sda.project.model.Project;
import com.sda.project.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project save(Project project) {
        log.info("save project {}", project);

        String name = project.getName();
        Optional<Project> existingProjectOptional = projectRepository.findByNameIgnoreCase(name);
        if (existingProjectOptional.isPresent()) {
            log.error("project with name {} already exists", name);
            throw new ResourceAlreadyExistsException("project with name " + name + " already exists");
        } else {
            return projectRepository.save(project);
        }
    }

    public List<Project> findAll() {
        log.info("find projects");

        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        log.info("find project id {}", id);

        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));
    }

    public void update(Project project) {
        log.info("update project {}", project);

        String name = project.getName();
        projectRepository.findByNameIgnoreCase(name)
                .filter(existingProject -> existingProject.getId().equals(project.getId()))
                .map(existingProject -> projectRepository.save(project))
                .orElseThrow(() -> {
                    log.error("project with name {} already exists", name);
                    throw new ResourceAlreadyExistsException("project with name " + name + " already exists");
                });
    }

    public void delete(Long id) {
        log.info("delete project {}", id);

        projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("project not found"));
        projectRepository.deleteById(id);
    }
}
