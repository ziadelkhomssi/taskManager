package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.mapper.TicketMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.SprintRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class TicketService {

    @Autowired
    private NotificationService notificationService;

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

    public PageResponse<TicketSummary> getSummaryList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<Ticket> page;
        switch (query.filterBy()) {
            case "ticket" -> {
                page = ticketRepository.findAllByTicketName(query.search(), pageable);
            }

            case "status" -> {
                page = ticketRepository.findAllByTicketStatus(query.search(), pageable);
            }
            case "user" -> {
                page = ticketRepository.findAllByUserName(query.search(), pageable);
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
                ticketMapper.toSummaryList(page.getContent())
        );
    }

    public void createTicket(TicketCreate command) {
        Sprint parentSprint = sprintRepository.findById(command.sprintId())
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found: " + command.sprintId()));
        User assignedUser = userRepository.findById(command.assignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + command.sprintId()));

        Ticket ticket = ticketMapper.createToEntity(command);
        ticket.setUser(assignedUser);
        ticket.setSprint(parentSprint);

        ticket = ticketRepository.save(ticket);
        notificationService.createNotification(ticket.getId(), NotificationType.TICKET_ASSIGNED);
    }

    public void updateTicket(TicketUpdate command) {
        Ticket old = ticketRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + command.id()));

        Ticket updated = ticketMapper.updateEntity(command, old);

        updated = ticketRepository.save(updated);
        notificationService.createNotification(updated.getId(), NotificationType.TICKET_ASSIGNED);
    }

    public void deleteTicketById(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("Ticket not found: " + id);
        }
        ticketRepository.deleteById(id);
    }
}
