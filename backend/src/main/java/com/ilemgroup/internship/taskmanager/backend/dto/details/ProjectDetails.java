package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectDetails(

        @NotNull
        @JsonProperty("id")
        Long id,

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

