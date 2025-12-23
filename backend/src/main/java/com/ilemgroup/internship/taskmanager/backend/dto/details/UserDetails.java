package com.ilemgroup.internship.taskmanager.backend.dto.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDetails(

        @NotNull
        @JsonProperty("id")
        String id,

        @NotBlank
        @JsonProperty("name")
        String name,

        @NotBlank
        @JsonProperty("job")
        String job,

        @JsonProperty("profilePictureUrl")
        String profilePictureUrl

) {}

