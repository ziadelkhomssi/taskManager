package com.ilemgroup.internship.taskmanager.backend.dto.summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserSummary(

        @NotBlank
        @JsonProperty("id")
        String id,

        @NotBlank
        @JsonProperty("name")
        String name,

        @JsonProperty("profilePictureUrl")
        String profilePictureUrl

) {}

