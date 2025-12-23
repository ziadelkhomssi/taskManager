package com.ilemgroup.internship.taskmanager.backend.dto.summary;


import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SprintSummary(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotNull
        @JsonProperty("startDate")
        LocalDateTime startDate,

        @NotNull
        @JsonProperty("dueDate")
        LocalDateTime dueDate,

        @NotNull
        @JsonProperty("status")
        SprintStatus status

) {}

