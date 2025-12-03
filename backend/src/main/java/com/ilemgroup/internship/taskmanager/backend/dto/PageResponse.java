package com.ilemgroup.internship.taskmanager.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PageResponse<T>(

        @NotNull
        @JsonProperty("currentPage")
        Long currentPage,

        @NotNull
        @JsonProperty("size")
        Long size,

        @NotNull
        @JsonProperty("totalElements")
        Long totalElements,

        @NotNull
        @JsonProperty("totalPages")
        Long totalPages,

        @NotNull
        @JsonProperty("content")
        List<T> content

) {}

