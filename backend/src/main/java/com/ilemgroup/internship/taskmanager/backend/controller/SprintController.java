package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.service.SprintService;
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

    @GetMapping("/details/{id}")
    public SprintDetails getDetailsById(@PathVariable Long id) {
        return sprintService.getDetailsById(id);
    }

    @GetMapping("/summary")
    public Page<SprintSummary> getSummaryList(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter
    ) {
        return sprintService.getSummaryList(pageable, search, filter);
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
