package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.mapper.ProjectMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional
public class ProjectService {
    private enum Filters {
        PROJECT,
        STATUS,
        SPRINT,
        USER
    }

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    public ProjectDetails getDetailsById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));

        return projectMapper.toDetails(project);
    }

    public Page<ProjectSummary> getSummaryList(
            Pageable pageable, 
            String search, 
            String filter
    ) {
        if (search == null || search.isBlank()) {
            return projectRepository.findAll(pageable).map(projectMapper::toSummary);
        }

        try {
            Filters.valueOf(filter.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return projectRepository.findAllWithFilter(
                search,
                filter.toUpperCase(),
                pageable
        ).map(projectMapper::toSummary);
    }

    public void createProject(ProjectCreate command) {
        Project project = projectMapper.createToEntity(command);
        projectRepository.save(project);
    }

    public void updateProject(ProjectUpdate command) {
        Project old = projectRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + command.id()));

        Project updated = projectMapper.updateEntity(command, old);

        projectRepository.save(updated);
    }

    public void deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found: " + id);
        }
        projectRepository.deleteById(id);
    }
}



