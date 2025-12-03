package com.ilemgroup.internship.taskmanager.backend.dto.command.create;

import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCreate(

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @NotNull
        @JsonProperty("assignedUserId")
        Long assignedUserId,

        @NotNull
        @JsonProperty("priority")
        TicketPriority priority,

        @NotNull
        @JsonProperty("status")
        TicketStatus status

) {}

