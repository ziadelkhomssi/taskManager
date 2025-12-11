package com.ilemgroup.internship.taskmanager.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthorizationServiceIntegrationTest {
    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void ensureSameUserOrAdmin_Success() throws AccessDeniedException {
        String clientId = "abc123";
        AuthorizationService.ensureSameUserOrAdmin(clientId);
    }

    @Test
    void ensureSameUserOrAdmin_AccessDenied_AuthenticationIsNull() {
        String clientId = "abc123";
        assertThrows(AccessDeniedException.class, () -> AuthorizationService.ensureSameUserOrAdmin(clientId));
    }

    @Test
    @WithMockUser(username = "HACKER", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void ensureSameUserOrAdmin_AccessDenied_NotSameUser() {
        String clientId = "abc123";
        assertThrows(AccessDeniedException.class, () -> AuthorizationService.ensureSameUserOrAdmin(clientId));
    }

    @Test
    @WithMockUser(username = "abc123", password = "mypasswordwoah", roles = {"TEAM_MEMBER"})
    void getClientUserId_Success() throws AccessDeniedException {
        String clientId = AuthorizationService.getClientUserId();
        assertEquals("abc123", clientId);
    }

    @Test
    void getClientUserId__AccessDenied_AuthenticationIsNull() {
        assertThrows(AccessDeniedException.class, AuthorizationService::getClientUserId);
    }
}
