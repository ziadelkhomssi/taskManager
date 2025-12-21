package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.SprintService;
import com.ilemgroup.internship.taskmanager.backend.service.TicketService;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sprint")
public class SprintController {
    @Autowired
    private SprintService sprintService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @GetMapping("/details/{id}")
    public SprintDetails getDetailsById(@PathVariable Long id) {
        return sprintService.getDetailsById(id);
    }

    @GetMapping("/{sprintId}/ticket/summary")
    public Page<TicketSummary> getTicketSummaryList(
            @PathVariable Long sprintId,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter
    ) {
        return ticketService.getSummaryList(sprintId, pageable, search, filter);
    }

    @GetMapping("/{sprintId}/participant/summary")
    public Page<UserSummary> getUserSummaryList(
            @PathVariable Long sprintId,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter)
    {
        return userService.getSprintParticipants(sprintId, pageable, search, filter);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void createSprint(@RequestBody @Valid SprintCreate command) {
        sprintService.createSprint(command);
    }

    // should i make this have pathvariable sprint id? (need to edit SprintUpdate for that...)
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void updateSprint(@RequestBody @Valid SprintUpdate command) {
        sprintService.updateSprint(command);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void deleteSprintById(@PathVariable Long id) {
        sprintService.deleteSprintById(id);
    }
}
