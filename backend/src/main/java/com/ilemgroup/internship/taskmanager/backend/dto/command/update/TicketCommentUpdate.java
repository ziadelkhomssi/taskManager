package com.ilemgroup.internship.taskmanager.backend.dto.command.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCommentUpdate(

        @NotNull
        @JsonProperty("id")
        Long id,

        @NotBlank
        @JsonProperty("comment")
        String comment

) {
}
