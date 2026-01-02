package com.ilemgroup.internship.taskmanager.backend.service;

import com.ilemgroup.internship.taskmanager.backend.dto.details.ClientPermissions;
import com.mysql.cj.xdevapi.Client;
import org.mapstruct.Named;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class AuthorizationService {
    public static final ClientPermissions ADMIN_PERMISSIONS = new ClientPermissions(
            true,
            true,
            true
    );
    public static final ClientPermissions TEAM_MEMBER_PERMISSIONS = new ClientPermissions(
            false,
            false,
            true
    );
    public static final ClientPermissions VIEWER_PERMISSIONS = new ClientPermissions(
            false,
            false,
            false
    );

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

    @Named("buildPermissionsFromRole")
    public static ClientPermissions buildPermissionsFromRole(String role) {
        return switch (role) {
            case "ADMIN", "PROJECT_MANAGER" -> ADMIN_PERMISSIONS;
            case "TEAM_MEMBER" -> TEAM_MEMBER_PERMISSIONS;
            default -> VIEWER_PERMISSIONS;
        };
    }
}
