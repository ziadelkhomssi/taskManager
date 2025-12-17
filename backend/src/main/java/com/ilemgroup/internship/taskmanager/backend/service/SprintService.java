package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.mapper.SprintMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class SprintService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintMapper sprintMapper;

    public SprintDetails getDetailsById(Long id) {
        Sprint sprint = sprintRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found: " + id));

        return sprintMapper.toDetails(sprint);
    }

    public Page<SprintSummary> getSummaryList(
            Pageable pageable, 
            String search, 
            String filter
    ) {
        Page<SprintSummary> page;

        switch (filter) {
            case "sprint" -> {
                page = sprintRepository.findAllBySprintName(search, pageable).map(sprintMapper::toSummary);
            }
            case "status" -> {
                page = sprintRepository.findAllBySprintStatus(search, pageable).map(sprintMapper::toSummary);
            }
            case "user" -> {
                page = sprintRepository.findAllByUserName(search, pageable).map(sprintMapper::toSummary);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return page;
    }

    public void createSprint(SprintCreate command) {
        Project parentProject = projectRepository.findById(command.projectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + command.projectId()));

        Sprint sprint = sprintMapper.createToEntity(command);
        sprint.setProject(parentProject);
        sprintRepository.save(sprint);
    }

    public void updateSprint(SprintUpdate command) {
        Sprint old = sprintRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found: " + command.id()));

        Sprint updated = sprintMapper.updateEntity(command, old);

        sprintRepository.save(updated);
    }

    public void deleteSprintById(Long id) {
        if (!sprintRepository.existsById(id)) {
            throw new EntityNotFoundException("Sprint not found: " + id);
        }
        sprintRepository.deleteById(id);
    }
}
