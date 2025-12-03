package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TicketCommentDetails(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotNull
        @JsonProperty("userSummary")
        UserSummary userSummary,

        @NotBlank
        @JsonProperty("comment")
        String comment,

        @JsonProperty("parentCommentId")
        Long parentCommentId,

        @NotNull
        @JsonProperty("createdAt")
        LocalDateTime createdAt,

        @JsonProperty("updatedAt")
        LocalDateTime updatedAt

) {}

