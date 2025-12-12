package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
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

    public PageResponse<SprintSummary> getSummaryList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<Sprint> page;

        switch (query.filterBy()) {
            case "sprint" -> {
                page = sprintRepository.findAllBySprintName(query.search(), pageable);
            }
            case "status" -> {
                page = sprintRepository.findAllBySprintStatus(query.search(), pageable);
            }
            case "user" -> {
                page = sprintRepository.findAllByUserName(query.search(), pageable);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + query.filterBy()
            );
        }

        return new PageResponse<>(
                query.page(),
                query.size(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                sprintMapper.toSummaryList(page.getContent())
        );
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
