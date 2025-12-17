package com.ilemgroup.internship.taskmanager.backend.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/secure")
    @PreAuthorize("hasRole('ADMIN')")
    public String secureEndpoint() {
        return "secured";
    }
}
