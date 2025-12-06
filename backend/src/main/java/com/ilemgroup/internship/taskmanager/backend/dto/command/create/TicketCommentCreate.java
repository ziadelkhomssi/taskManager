package com.ilemgroup.internship.taskmanager.backend.dto.command.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCommentCreate(

        @NotBlank
        @JsonProperty("comment")
        String comment,

        @NotNull
        @JsonProperty("ticketId")
        Long ticketId,

        @JsonProperty("parentCommentId")
        Long parentCommentId

) {
}
