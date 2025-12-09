package com.ilemgroup.internship.taskmanager.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PageQuery(

        @NotNull
        @JsonProperty("page")
        @Min(0)
        Integer page,

        @NotNull
        @JsonProperty("size")
        @Min(1)
        Integer size,

        @JsonProperty("search")
        String search,

        @JsonProperty("filterBy")
        String filterBy

) {}

