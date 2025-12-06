package com.ilemgroup.internship.taskmanager.backend.mapper;


import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class)
public interface ProjectMapper {

    ProjectSummary toSummary(Project project);
    List<ProjectSummary> toSummaryList(List<Project> projectList);

    ProjectDetails toDetails(Project project);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sprints", ignore = true)
    Project createToEntity(ProjectCreate dto);

    @Mapping(target = "sprints", ignore = true)
    Project updateEntity(ProjectUpdate dto, @MappingTarget Project project);
}
