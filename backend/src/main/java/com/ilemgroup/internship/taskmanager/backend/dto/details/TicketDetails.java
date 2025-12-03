package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketDetails(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @NotNull
        @JsonProperty("userSummary")
        UserSummary userSummary,

        @NotNull
        @JsonProperty("priority")
        TicketPriority priority,

        @NotNull
        @JsonProperty("status")
        TicketStatus status,

        @JsonProperty("closedAt")
        LocalDateTime closedAt

) {}

