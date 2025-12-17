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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ProjectService {

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
        Page<ProjectSummary> page;

        switch (filter) {
            case "project" -> {
                page = projectRepository.findAllByProjectName(search, pageable).map(projectMapper::toSummary);
            }
            case "status" -> {
                page = projectRepository.findAllByProjectStatus(search, pageable).map(projectMapper::toSummary);
            }
            case "sprint" -> {
                page = projectRepository.findAllBySprintName(search, pageable).map(projectMapper::toSummary);
            }
            case "user" -> {
                page = projectRepository.findAllByUserName(search, pageable).map(projectMapper::toSummary);
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unknown filter: " + filter
            );
        }

        return page;
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



