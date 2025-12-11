package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public PageResponse<NotificationDetails> getDetailsList(@RequestBody @Valid PageQuery query) throws AccessDeniedException {
        return notificationService.getDetailsList(query);
    }

    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) throws AccessDeniedException {
        notificationService.markAsRead(id);
    }
}
