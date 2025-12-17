package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.SprintCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.SprintUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.SprintSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.SprintStatus;
import com.ilemgroup.internship.taskmanager.backend.service.SprintService;
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
@ContextConfiguration(classes = SprintController.class)
public class SprintControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SprintService sprintService;

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
                any(Pageable.class),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/sprint/summary")
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
    void createSprint_userHasValidRole_success() throws Exception {
        SprintCreate validCommand = new SprintCreate(
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE,
                1L
        );

        mockMvc.perform(post("/sprint/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createSprint_userHasInvalidRole_throwsException() throws Exception {
        SprintCreate validCommand = new SprintCreate(
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE,
                1L
        );

        mockMvc.perform(post("/sprint/create")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createSprint_validCommand_success() throws Exception {
        SprintCreate validCommand = new SprintCreate(
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE,
                1L
        );

        mockMvc.perform(post("/sprint/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createSprint_invalidCommand_throwsException() throws Exception {
        SprintCreate invalidCommand = new SprintCreate(
                "",
                "",
                null,
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE,
                1L
        );

        mockMvc.perform(post("/sprint/create")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSprint_validCommand_success() throws Exception {
        SprintUpdate validCommand = new SprintUpdate(
                1L,
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE
        );

        mockMvc.perform(put("/sprint/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSprint_invalidCommand_throwsException() throws Exception {
        SprintUpdate invalidCommand = new SprintUpdate(
                null,
                "",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE
        );

        mockMvc.perform(put("/sprint/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSprint_userHasValidRole_success() throws Exception {
        SprintUpdate validCommand = new SprintUpdate(
                1L,
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE
        );


        mockMvc.perform(put("/sprint/update")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateSprint_userHasInvalidRole_throwsException() throws Exception {
        SprintUpdate validCommand = new SprintUpdate(
                1L,
                "Sprint",
                "Description",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                SprintStatus.ACTIVE
        );

        mockMvc.perform(put("/sprint/update")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteSprint_userHasValidRole_success() throws Exception {
        mockMvc.perform(delete("/sprint/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSprint_userHasInvalidRole_throwsException() throws Exception {
        mockMvc.perform(delete("/sprint/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("some_guy")))
                .andExpect(status().isForbidden());
    }
}
