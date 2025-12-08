package com.ilemgroup.internship.taskmanager.backend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Service
public class AuthorizationService {
    public static void ensureSameUserOrAdmin(String idToCompare) throws AccessDeniedException {
        String clientUserId = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if (!idToCompare.equalsIgnoreCase(clientUserId)) {
            throw new AccessDeniedException(
                    "User with id " + clientUserId + " does not have permission!"
            );
        }
    }
}
