package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@EnableMethodSecurity
@ContextConfiguration(classes = UserController.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    
    @Test
    void getSummaryList_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "user"
        );
        PageResponse<UserSummary> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new UserSummary(
                                "abc123",
                                "John Developer",
                                "profilePicture.png"
                        )
                )
        );

        when(userService.getSummaryList(any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/user/summary")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
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

        mockMvc.perform(get("/user/summary")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectParticipants_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "user"
        );
        PageResponse<UserSummary> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new UserSummary(
                                "abc123",
                                "John Developer",
                                "profilePicture.png"
                        )
                )
        );

        when(userService.getProjectParticipants(any(Long.class), any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/user/participants/project/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validQuery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getProjectParticipants_invalidQuery_throwsException() throws Exception {
        PageQuery invalidQuery = new PageQuery(
                -1,
                -1,
                null,
                null
        );

        mockMvc.perform(get("/user/participants/project/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getSprintParticipants_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "user"
        );
        PageResponse<UserSummary> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new UserSummary(
                                "abc123",
                                "John Developer",
                                "profilePicture.png"
                        )
                )
        );

        when(userService.getSprintParticipants(any(Long.class), any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/user/participants/sprint/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validQuery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getSprintParticipants_invalidQuery_throwsException() throws Exception {
        PageQuery invalidQuery = new PageQuery(
                -1,
                -1,
                null,
                null
        );

        mockMvc.perform(get("/user/participants/sprint/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }
}
