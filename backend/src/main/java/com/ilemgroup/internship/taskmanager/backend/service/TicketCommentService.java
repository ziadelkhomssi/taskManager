package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.entity.Ticket;
import com.ilemgroup.internship.taskmanager.backend.entity.TicketComment;
import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.mapper.TicketCommentMapper;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketCommentRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.TicketRepository;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TicketCommentService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketCommentRepository ticketCommentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketCommentMapper ticketCommentMapper;

    public List<TicketCommentDetails> getDetailsList(Long ticketId, PageQuery query) {
        Pageable pageable = PageRequest.of(query.page(), query.size());
        Page<TicketComment> page = ticketCommentRepository.findAllByTicketId(ticketId, pageable);
        return ticketCommentMapper.toDetailsList(page.getContent());
    }

    public void createTicketComment(TicketCommentCreate command) {
        Ticket ticket = ticketRepository.findById(command.ticketId())
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found: " + command.ticketId()));
        User user = userRepository.findById(
                        Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName()
                )
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + command.ticketId()));

        TicketComment comment = ticketCommentMapper.createToEntity(command);
        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        if (command.parentCommentId() != null) {
            TicketComment parentComment = ticketCommentRepository.findById(command.parentCommentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Parent comment not found: " + command.parentCommentId())
                    );
            comment.setParentComment(parentComment);
        }

        ticketCommentRepository.save(comment);
    }

    public void updateTicketComment(TicketCommentUpdate command) throws AccessDeniedException {
        TicketComment old = ticketCommentRepository.findById(command.id())
                .orElseThrow(() -> new EntityNotFoundException("Ticket comment not found: " + command.id()));

        AuthorizationService.ensureSameUserOrAdmin(
                old.getUser().getAzureOid()
        );

        TicketComment updated = ticketCommentMapper.updateEntity(command, old);
        updated.setCreatedAt(LocalDateTime.now());

        ticketCommentRepository.save(updated);
    }

    public void deleteTicketCommentById(Long id) throws AccessDeniedException {
        TicketComment existing = ticketCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket comment not found: " + id));

        AuthorizationService.ensureSameUserOrAdmin(
                existing.getUser().getAzureOid()
        );
        ticketCommentRepository.deleteById(id);
    }
}
