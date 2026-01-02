package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientDetails(

        @NotBlank
        @JsonProperty("userId")
        String userId,

        @NotBlank
        @JsonProperty("name")
        String name,

        @JsonProperty("profilePictureUrl")
        String profilePictureUrl,

        @NotBlank
        @JsonProperty("permissions")
        ClientPermissions permissions

) { }

