package com.ilemgroup.internship.taskmanager.backend.dto.summary;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SprintSummary(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotNull
        @JsonProperty("startDate")
        Instant startDate,

        @NotNull
        @JsonProperty("dueDate")
        Instant dueDate,

        @NotNull
        @JsonProperty("status")
        SprintStatus status

) {}

