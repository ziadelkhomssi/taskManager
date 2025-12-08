package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.mapper.UserMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
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
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDetails getDetailsById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        return userMapper.toDetails(user);
    }

    public List<UserSummary> getSummaryList(PageQuery query) {
        Specification<@NonNull User> specification = buildSummaryListSpecification(query);
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull User> page = userRepository.findAll(specification, pageable);

        return userMapper.toSummaryList(page.getContent());
    }

    public List<UserSummary> getProjectParticipants(Long projectId, PageQuery query) {
        Specification<@NonNull User> specification = buildProjectParticipantsSpecification(projectId, query);
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull User> page = userRepository.findAll(specification, pageable);

        return userMapper.toSummaryList(page.getContent());
    }

    public List<UserSummary> getSprintParticipants(Long sprintId, PageQuery query) {
        Specification<@NonNull User> specification = buildSprintParticipantsSpecification(sprintId, query);
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull User> page = userRepository.findAll(specification, pageable);

        return userMapper.toSummaryList(page.getContent());
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        User existing = userRepository.findById(user.getAzureOid())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + user.getAzureOid()));

        userRepository.save(user);
    }

    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private Specification<@NonNull User> buildSummaryListSpecification(PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "name" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), like)
                    );
                }
                case "job" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("job")), like)
                    );
                }

                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown filter: " + query.filterBy()
                );
            }

            return predicate;
        };
    }

    private Specification<@NonNull User> buildProjectParticipantsSpecification(Long projectId, PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<User, Ticket> ticketJoin = root.join("tickets");
            Join<Ticket, Sprint> sprintJoin = ticketJoin.join("sprint");
            Join<Sprint, Project> projectJoin = sprintJoin.join("project");
            Predicate predicate = criteriaBuilder.equal(projectJoin.get("id"), projectId);

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "name" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), like)
                    );
                }
                case "job" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("job")), like)
                    );
                }

                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown filter: " + query.filterBy()
                );
            }

            return predicate;
        };
    }

    private Specification<@NonNull User> buildSprintParticipantsSpecification(Long projectId, PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<User, Ticket> ticketJoin = root.join("tickets");
            Join<Ticket, Sprint> sprintJoin = ticketJoin.join("sprint");
            Predicate predicate = criteriaBuilder.equal(sprintJoin.get("id"), projectId);

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "name" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), like)
                    );
                }
                case "job" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("job")), like)
                    );
                }

                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Unknown filter: " + query.filterBy()
                );
            }

            return predicate;
        };
    }
}
