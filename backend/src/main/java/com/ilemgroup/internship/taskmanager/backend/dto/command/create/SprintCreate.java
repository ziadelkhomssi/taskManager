package com.ilemgroup.internship.taskmanager.backend.dto.command.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SprintCreate(

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
        SprintStatus status,

        @NotNull
        @JsonProperty("projectId")
        Long projectId

) {}

