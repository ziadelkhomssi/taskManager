package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/details/{id}")
    public TicketDetails getDetailsById(@PathVariable Long id) {
        return ticketService.getDetailsById(id);
    }

    @GetMapping("/summary")
    public Page<TicketSummary> getSummaryList(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter
    ) {
        return ticketService.getSummaryList(pageable, search, filter);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void createTicket(@RequestBody @Valid TicketCreate command) {
        ticketService.createTicket(command);
    }

    // should i make this have pathvariable ticket id? (need to edit TicketUpdate for that...)
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void updateTicket(@RequestBody @Valid TicketUpdate command) {
        ticketService.updateTicket(command);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'TEAM_MEMBER')")
    public void deleteTicketById(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
    }
}
