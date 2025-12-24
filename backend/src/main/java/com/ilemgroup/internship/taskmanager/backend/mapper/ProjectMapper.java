package com.ilemgroup.internship.taskmanager.backend.mapper;


import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.ProjectDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.Project;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = CentralMapperConfiguration.class)
public interface ProjectMapper {

    @Mapping(
            target = "profilePictureUrl",
            expression = "java(baseUrl + \"/project/profile-picture/\" + project.getId())"
    )
    ProjectSummary toSummary(Project project, @Context String baseUrl);
    List<ProjectSummary> toSummaryList(List<Project> projectList);

    @Mapping(
            target = "profilePictureUrl",
            expression = "java(baseUrl + \"/project/profile-picture/\" + project.getId())"
    )
    ProjectDetails toDetails(Project project, @Context String baseUrl);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sprints", ignore = true)
    Project createToEntity(ProjectCreate command);

    @Mapping(target = "sprints", ignore = true)
    Project updateEntity(ProjectUpdate command, @MappingTarget Project project);
}
