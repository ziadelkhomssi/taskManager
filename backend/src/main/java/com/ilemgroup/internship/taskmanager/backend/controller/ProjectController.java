package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.ProjectService;
import com.ilemgroup.internship.taskmanager.backend.service.SprintService;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private SprintService sprintService;
    @Autowired
    private UserService userService;

    @GetMapping("/details/{id}")
    public ProjectDetails getDetailsById(@PathVariable Long id) {
        return projectService.getDetailsById(id);
    }

    @GetMapping("/summary")
    public Page<ProjectSummary> getSummaryList(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter
    ) {
        return projectService.getSummaryList(pageable, search, filter);
    }

    @GetMapping("/{projectId}/sprint/summary")
    public Page<SprintSummary> getSprintSummaryList(
            @PathVariable Long projectId,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter
    ) {
        return sprintService.getSummaryList(projectId, pageable, search, filter);
    }

    @GetMapping("/{projectId}/participant/summary")
    public Page<UserSummary> getUserSummaryList(
            @PathVariable Long projectId,
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter)
    {
        return userService.getProjectParticipants(projectId, pageable, search, filter);
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void createProject(
            @RequestPart("command") @Valid ProjectCreate command,
            @RequestPart("profilePicture") MultipartFile profilePicture
    ) {
        projectService.createProject(command, profilePicture);
    }

    // should i make this have pathvariable project id? (need to edit ProjectUpdate for that...)
    @PutMapping(path = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void updateProject(
            @RequestPart("command") @Valid ProjectUpdate command,
            @RequestPart("profilePicture") MultipartFile profilePicture) {
        projectService.updateProject(command, profilePicture);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER')")
    public void deleteProjectById(@PathVariable Long id) {
        projectService.deleteProjectById(id);
    }

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long id) throws IOException {
        Resource resource = projectService.getProfilePicture(id);

        if (resource == null || !resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        String contentType = "image/png";
        if (resource instanceof UrlResource) {
            String filename = resource.getFilename();
            if (filename != null && filename.contains(".")) {
                String extension = filename.substring(filename.lastIndexOf(".") + 1);
                contentType = "image/" + extension;
            }
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
