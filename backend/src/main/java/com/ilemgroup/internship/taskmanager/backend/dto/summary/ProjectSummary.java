package com.ilemgroup.internship.taskmanager.backend.dto.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectSummary(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("title")
        String title,

        @JsonProperty("profilePicture")
        String profilePicture,

        @NotNull
        @JsonProperty("status")
        ProjectStatus status

) {}
