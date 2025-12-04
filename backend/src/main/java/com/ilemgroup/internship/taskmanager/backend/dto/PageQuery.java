package com.ilemgroup.internship.taskmanager.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record PageQuery(

        @NotNull
        @JsonProperty("page")
        Integer page,

        @NotNull
        @JsonProperty("size")
        Integer size,

        @JsonProperty("search")
        String search,

        @JsonProperty("filterBy")
        String filterBy

) {}

