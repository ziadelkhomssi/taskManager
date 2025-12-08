package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import com.ilemgroup.internship.taskmanager.backend.mapper.SprintMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.ProjectRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
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

    public List<SprintSummary> getSummaryList(PageQuery query) {
        Specification<@NonNull Sprint> specification = buildSummaryListSpecification(query);

        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull Sprint> page = sprintRepository.findAll(specification, pageable);

        return sprintMapper.toSummaryList(page.getContent());
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

    private Specification<@NonNull Sprint> buildSummaryListSpecification(PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "sprint" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), like));
                }

                case "status" -> {
                    try {
                        SprintStatus status = SprintStatus.valueOf(search);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get("status"), status));
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid sprint status: " + search
                        );
                    }
                }
                case "user" -> {
                    Join<Sprint, Ticket> ticketJoin = root.join("tickets");
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
