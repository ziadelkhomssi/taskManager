package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.details.UserDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/details/{id}")
    public UserDetails getDetailsById(@PathVariable String id) {
        return userService.getDetailsById(id);
    }

    @GetMapping("/summary/client")
    public UserSummary getClientSummary() throws AccessDeniedException {
        return userService.getClientSummary();
    }

    @GetMapping("/summary")
    public Page<UserSummary> getAllUsers(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter)
    {
        return userService.getAllUsers(pageable, search, filter);
    }

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String id) throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(new InputStreamResource(userService.getProfilePicture(id).getInputStream()));
    }
}
