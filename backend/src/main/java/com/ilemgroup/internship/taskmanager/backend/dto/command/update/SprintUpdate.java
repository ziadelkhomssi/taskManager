package com.ilemgroup.internship.taskmanager.backend.dto.command.update;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SprintUpdate(

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
        LocalDate startDate,

        @NotNull
        @JsonProperty("dueDate")
        LocalDate dueDate,

        @NotNull
        @JsonProperty("status")
        SprintStatus status

) {}

