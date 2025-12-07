package com.ilemgroup.internship.taskmanager.backend.dto.command.update;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketUpdate(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @NotBlank
        @JsonProperty("assignedUserId")
        String assignedUserId,

        @NotNull
        @JsonProperty("priority")
        TicketPriority priority,

        @NotNull
        @JsonProperty("status")
        TicketStatus status

) {}

