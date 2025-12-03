package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NotificationDetails(

        @NotNull
        @JsonProperty("type")
        NotificationType type,

        @NotNull
        @JsonProperty("ticketSummary")
        TicketSummary ticketSummary,

        @NotNull
        @JsonProperty("isRead")
        boolean isRead,

        @NotNull
        @JsonProperty("readAt")
        LocalDateTime readAt

) {}

