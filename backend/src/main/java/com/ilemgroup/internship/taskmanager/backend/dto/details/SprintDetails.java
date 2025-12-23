package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SprintDetails(

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
        @JsonProperty("startDate")
        LocalDateTime startDate,

        @NotNull
        @JsonProperty("dueDate")
        LocalDateTime dueDate,

        @JsonProperty("endDate")
        LocalDateTime endDate,

        @NotNull
        @JsonProperty("status")
        SprintStatus status

) {}

