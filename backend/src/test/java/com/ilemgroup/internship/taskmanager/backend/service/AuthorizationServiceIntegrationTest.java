package com.ilemgroup.internship.taskmanager.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthorizationServiceIntegrationTest {
    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testEnsureSameUserOrAdmin() throws AccessDeniedException {
        String clientId = "abc123";
        AuthorizationService.ensureSameUserOrAdmin(clientId);
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void testEnsureSameUserOrAdmin_AccessDenied() {
        String clientId = "HACKER";
        assertThrows(AccessDeniedException.class, () -> AuthorizationService.ensureSameUserOrAdmin(clientId));
    }
}
