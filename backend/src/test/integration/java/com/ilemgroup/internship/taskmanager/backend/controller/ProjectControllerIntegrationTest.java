package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.ProjectCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.ProjectUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import com.ilemgroup.internship.taskmanager.backend.service.ProjectService;
import com.ilemgroup.internship.taskmanager.backend.service.SprintService;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
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
    @MockitoBean
    private SprintService sprintService;
    @MockitoBean
    private UserService userService;

    @Test
    void getSummaryList_validQuery_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));

        Page<ProjectSummary> page = new PageImpl<>(
                List.of(
                        new ProjectSummary(
                                1L,
                                "Project",
                                "profilePicture.png",
                                ProjectStatus.ACTIVE
                        )
                ),
                pageable,
                1
        );

        when(projectService.getSummaryList(
                any(Pageable.class),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/project/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title")
                        .param("search", "")
                        .param("filter", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    /*@Test
    void getSummaryList_invalidQuery_throwsException() throws Exception {
        mockMvc.perform(get("/project/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/
    
    @Test
    void getSprintSummaryList_validQuery_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));

        Page<SprintSummary> page = new PageImpl<>(
                List.of(
                        new SprintSummary(
                                1L,
                                "Sprint",
                                LocalDate.now(),
                                LocalDate.now().plusDays(7),
                                SprintStatus.ACTIVE
                        )
                ),
                pageable,
                1
        );

        when(sprintService.getSummaryList(
                any(Long.class),
                any(Pageable.class),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/project/1/sprint/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "title")
                        .param("search", "")
                        .param("filter", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    /*@Test
    void getSprintSummaryList_invalidQuery_throwsException() throws Exception {
        mockMvc.perform(get("/sprint/summary")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/


    @Test
    void getUserSummaryList_validQuery_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("user"));

        Page<UserSummary> page = new PageImpl<>(
                List.of(
                        new UserSummary(
                                "abc123",
                                "John Developer",
                                "profilePicture.png"
                        )
                ),
                pageable,
                1
        );

        when(userService.getProjectParticipants(
                any(Long.class),
                any(Pageable.class),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/project/1/participant/summary")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "user")
                        .param("search", "")
                        .param("filter", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    /*@Test
    void getUserSummaryList_invalidQuery_throwsException() throws Exception {
        mockMvc.perform(get("/user/participants/project/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/

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
