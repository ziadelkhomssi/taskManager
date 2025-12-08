package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import com.ilemgroup.internship.taskmanager.backend.mapper.ProjectMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<ProjectSummary> getSummaryList(PageQuery query) {
        Specification<@NonNull Project> specification = buildSummaryListSpecification(query);

        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull Project> page = projectRepository.findAll(specification, pageable);

        return projectMapper.toSummaryList(page.getContent());
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

    private Specification<@NonNull Project> buildSummaryListSpecification(PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "project" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), like));
                }
                case "sprint" -> {
                    Join<Project, Sprint> sprintJoin = root.join("sprints");
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(sprintJoin.get("title")), like));
                }
                case "status" -> {
                    try {
                        ProjectStatus status = ProjectStatus.valueOf(search);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get("status"), status));
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid project status: " + search
                        );
                    }
                }
                case "user" -> {
                    Join<Project, Sprint> sprintJoin = root.join("sprints");
                    Join<Sprint, Ticket> ticketJoin = sprintJoin.join("tickets");
                    Join<Ticket, User> userJoin = ticketJoin.join("user");

                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), like));
                }
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown filter: " + query.filterBy()
                );
            }

            return predicate;
        };
    }
}



