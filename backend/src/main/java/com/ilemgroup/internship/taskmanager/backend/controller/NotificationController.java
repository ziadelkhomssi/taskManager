package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/details")
    public Page<NotificationDetails> getAllForClient(
            Pageable pageable
    ) throws AccessDeniedException {
        return notificationService.getAllForClient(pageable);
    }

    @GetMapping("/has-unread")
    public ResponseEntity<Boolean> getHasUnread(
            Pageable pageable
    ) throws AccessDeniedException {
        boolean hasUnread = notificationService.hasUnreadNotificationsForClient();
        return ResponseEntity.ok(hasUnread);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) throws AccessDeniedException {
        notificationService.markAsRead(id);
    }
}
