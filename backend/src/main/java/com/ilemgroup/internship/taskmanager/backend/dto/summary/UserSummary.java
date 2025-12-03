package com.ilemgroup.internship.taskmanager.backend.dto.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserSummary(

        @NotBlank
        @JsonProperty("id")
        String id,

        @NotBlank
        @JsonProperty("name")
        String name,

        @JsonProperty("profilePicture")
        String profilePicture

) {}

