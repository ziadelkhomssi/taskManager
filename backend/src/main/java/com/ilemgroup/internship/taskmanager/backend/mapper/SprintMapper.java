package com.ilemgroup.internship.taskmanager.backend.mapper;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.SprintDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Sprint;
import org.mapstruct.*;

@Mapper(config = CentralMapperConfiguration.class)
public interface SprintMapper {

    SprintSummary toSummary(Sprint sprint);

    SprintDetails toDetails(Sprint sprint);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Sprint createToEntity(SprintCreate dto);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Sprint updateToEntity(SprintUpdate dto);

}
