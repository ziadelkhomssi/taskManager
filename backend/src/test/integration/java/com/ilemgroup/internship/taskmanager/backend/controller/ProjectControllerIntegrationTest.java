package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import com.ilemgroup.internship.taskmanager.backend.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@EnableMethodSecurity
@ContextConfiguration(classes = ProjectController.class)
public class ProjectControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Test
    void getSummaryList_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "project"
        );
        PageResponse<ProjectSummary> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new ProjectSummary(
                                1L,
                                "Project",
                                "profilePicture.png",
                                ProjectStatus.ACTIVE
                        )
                )
        );

        when(projectService.getSummaryList(any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/project/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validQuery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getSummaryList_invalidQuery_throwsException() throws Exception {
        PageQuery invalidQuery = new PageQuery(
                -1,
                -1,
                null,
                null
        );

        mockMvc.perform(get("/project/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProject_userHasValidRole_success() throws Exception {
        ProjectCreate validCommand = new ProjectCreate(
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(post("/project/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createProject_userHasInvalidRole_throwsException() throws Exception {
        ProjectCreate validCommand = new ProjectCreate(
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(post("/project/create")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createProject_validCommand_success() throws Exception {
        ProjectCreate validCommand = new ProjectCreate(
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(post("/project/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createProject_invalidCommand_throwsException() throws Exception {
        ProjectCreate invalidCommand = new ProjectCreate(
                "",
                "",
                null,
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(post("/project/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProject_validCommand_success() throws Exception {
        ProjectUpdate validCommand = new ProjectUpdate(
                1L,
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(put("/project/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProject_invalidCommand_throwsException() throws Exception {
        ProjectUpdate invalidCommand = new ProjectUpdate(
                null,
                "",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(put("/project/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProject_userHasValidRole_success() throws Exception {
        ProjectUpdate validCommand = new ProjectUpdate(
                1L,
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );


        mockMvc.perform(put("/project/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateProject_userHasInvalidRole_throwsException() throws Exception {
        ProjectUpdate validCommand = new ProjectUpdate(
                1L,
                "Project",
                "Description",
                "profilePicture.png",
                ProjectStatus.ACTIVE
        );

        mockMvc.perform(put("/project/update")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteProject_userHasValidRole_success() throws Exception {
        mockMvc.perform(delete("/project/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProject_userHasInvalidRole_throwsException() throws Exception {
        mockMvc.perform(delete("/project/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("some_guy")))
                .andExpect(status().isForbidden());
    }
}
