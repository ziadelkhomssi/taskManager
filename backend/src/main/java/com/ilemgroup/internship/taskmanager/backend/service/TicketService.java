package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import com.ilemgroup.internship.taskmanager.backend.mapper.TicketMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
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
public class TicketService {

    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketMapper ticketMapper;

    public TicketDetails getDetailsById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + id));

        return ticketMapper.toDetails(ticket);
    }

    public List<TicketSummary> getSummaryList(PageQuery query) {
        Specification<@NonNull Ticket> specification = buildSummaryListSpecification(query);

        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<@NonNull Ticket> page = ticketRepository.findAll(specification, pageable);

        return ticketMapper.toSummaryList(page.getContent());
    }

    public void createTicket(TicketCreate command) {
        Sprint parentSprint = sprintRepository.findById(command.sprintId())
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found: " + command.sprintId()));
        User assignedUser = userRepository.findById(command.assignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + command.sprintId()));

        Ticket ticket = ticketMapper.createToEntity(command);
        ticket.setUser(assignedUser);
        ticket.setSprint(parentSprint);

        ticketRepository.save(ticket);
    }

    public void updateTicket(TicketUpdate command) {
        Ticket old = ticketRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + command.id()));

        Ticket updated = ticketMapper.updateEntity(command, old);

        ticketRepository.save(updated);
    }

    public void deleteTicketById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found: " + id);
        }
        ticketRepository.deleteById(id);
    }

    private Specification<@NonNull Ticket> buildSummaryListSpecification(PageQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            String search = query.search();
            if (search == null || search.isBlank()) {
                return predicate;
            }

            String like = "%" + search.toLowerCase() + "%";
            String filter = query.filterBy().toLowerCase();

            switch (filter) {
                case "ticket" -> {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), like));
                }

                case "status" -> {
                    try {
                        TicketStatus status = TicketStatus.valueOf(search);
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(root.get("status"), status));
                    } catch (IllegalArgumentException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Invalid ticket status: " + search
                        );
                    }
                }
                case "user" -> {
                    Join<Ticket, User> userJoin = root.join("user");

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
