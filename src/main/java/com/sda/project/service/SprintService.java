package com.sda.project.service;

import com.sda.project.controller.exception.ResourceAlreadyExistsException;
import com.sda.project.controller.exception.ResourceNotFoundException;
import com.sda.project.model.Sprint;
import com.sda.project.repository.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintService {

    private static final Logger log = LoggerFactory.getLogger(SprintService.class);

    private final SprintRepository sprintRepository;

    @Autowired
    public SprintService(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    public Sprint save(Sprint sprint) {
        log.info("save sprint {}", sprint);

        return sprintRepository.save(sprint);
    }

    public List<Sprint> findAll() {
        log.info("find sprints");

        return sprintRepository.findAll();
    }

    public Sprint findById(Long id) {
        log.info("find sprint id {}", id);

        return sprintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("sprint not found"));
    }

    public void update(Sprint sprint) {
        log.info("update sprint {}", sprint);

        String name = sprint.getName();
        sprintRepository.findByNameIgnoreCase(name)
                .filter(existingSprint -> existingSprint.getId().equals(sprint.getId()))
                .map(existingSprint -> sprintRepository.save(sprint))
                .orElseThrow(() -> {
                    log.error("sprint with name {} already exists", name);
                    throw new ResourceAlreadyExistsException("sprint with name " + name + " already exists");
                });
    }

    public void delete(Long id) {
        log.info("delete sprint {}", id);

        sprintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("sprint not found"));
        sprintRepository.deleteById(id);
    }
}
