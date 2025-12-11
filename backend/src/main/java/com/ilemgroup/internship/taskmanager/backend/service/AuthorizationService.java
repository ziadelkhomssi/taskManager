package com.ilemgroup.internship.taskmanager.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class AuthorizationService {
    public static void ensureSameUserOrAdmin(String idToCompare) throws AccessDeniedException {
        String clientUserId = getClientUserId();

        if (!idToCompare.equalsIgnoreCase(clientUserId)) {
            throw new AccessDeniedException(
                    "User with id " + clientUserId + " does not have permission!"
            );
        }
    }

    public static String getClientUserId() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated!");
        }

        return authentication.getName();
    }
}
