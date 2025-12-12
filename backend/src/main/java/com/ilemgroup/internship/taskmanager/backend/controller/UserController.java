package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/details/{id}")
    public UserDetails getDetailsById(@PathVariable String id) {
        return userService.getDetailsById(id);
    }

    @GetMapping("/summary")
    public PageResponse<UserSummary> getSummaryList(@RequestBody @Valid PageQuery query) {
        return userService.getSummaryList(query);
    }

    @GetMapping("/participants/project/{projectId}")
    public PageResponse<UserSummary> getProjectParticipants(@PathVariable Long projectId, @RequestBody @Valid PageQuery query) {
        return userService.getProjectParticipants(projectId, query);
    }

    @GetMapping("/participants/sprint/{sprintId}")
    public PageResponse<UserSummary> getSprintParticipants(@PathVariable Long sprintId, @RequestBody @Valid PageQuery query) {
        return userService.getSprintParticipants(sprintId, query);
    }
}
