package com.ilemgroup.internship.taskmanager.backend.dto.command.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SprintCreate(

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @NotNull
        @JsonProperty("startDate")
        Instant startDate,

        @NotNull
        @JsonProperty("dueDate")
        Instant dueDate,

        @JsonProperty("endDate")
        Instant endDate,

        @NotNull
        @JsonProperty("status")
        SprintStatus status,

        @NotNull
        @JsonProperty("projectId")
        Long projectId

) {}

