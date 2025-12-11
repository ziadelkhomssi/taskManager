package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/details/{id}")
    public ProjectDetails getDetailsById(@PathVariable Long id) {
        return projectService.getDetailsById(id);
    }

    @GetMapping("/summary")
    public PageResponse<ProjectSummary> getSummaryList(@RequestBody @Valid PageQuery query) {
        return projectService.getSummaryList(query);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void createProject(@RequestBody @Valid ProjectCreate command) {
        projectService.createProject(command);
    }

    // should i make this have pathvariable project id? (need to edit ProjectUpdate for that...)
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void updateProject(@RequestBody @Valid ProjectUpdate command) {
        projectService.updateProject(command);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }
}
