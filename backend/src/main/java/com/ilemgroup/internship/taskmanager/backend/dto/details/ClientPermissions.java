package com.ilemgroup.internship.taskmanager.backend.dto.details;

public record ClientPermissions(
        boolean canManipulateProject,
        boolean canManipulateSprint,
        boolean canManipulateTicket
) {
}
