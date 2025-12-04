package com.ilemgroup.internship.taskmanager.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PageResponse<T>(

        @NotNull
        @JsonProperty("currentPage")
        Integer currentPage,

        @NotNull
        @JsonProperty("size")
        Integer size,

        @NotNull
        @JsonProperty("totalElements")
        Integer totalElements,

        @NotNull
        @JsonProperty("totalPages")
        Integer totalPages,

        @NotNull
        @JsonProperty("content")
        List<T> content

) {}

