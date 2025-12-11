package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.service.TicketCommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/ticketComment")
public class TicketCommentController {
    @Autowired
    private TicketCommentService ticketCommentService;

    @GetMapping("/all/{ticketId}")
    public PageResponse<TicketCommentDetails> getDetailsList(@PathVariable Long ticketId, @RequestBody @Valid PageQuery query) {
        return ticketCommentService.getDetailsList(ticketId, query);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void createTicketComment(@RequestBody @Valid TicketCommentCreate command) {
        ticketCommentService.createTicketComment(command);
    }

    // should i make this have pathvariable ticketComment id? (need to edit TicketCommentUpdate for that...)
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void updateTicketComment(@RequestBody @Valid TicketCommentUpdate command) throws AccessDeniedException {
        ticketCommentService.updateTicketComment(command);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void deleteTicketCommentById(@PathVariable Long id) throws AccessDeniedException {
        ticketCommentService.deleteTicketCommentById(id);
    }
}
