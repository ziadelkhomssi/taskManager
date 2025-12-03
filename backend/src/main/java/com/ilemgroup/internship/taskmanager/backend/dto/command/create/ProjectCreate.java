package com.ilemgroup.internship.taskmanager.backend.dto.command.create;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectCreate(

        @NotBlank
        @JsonProperty("title")
        String title,

        @NotBlank
        @JsonProperty("description")
        String description,

        @JsonProperty("profilePicture")
        String profilePicture,

        @NotNull
        @JsonProperty("status")
        ProjectStatus status

) {}

