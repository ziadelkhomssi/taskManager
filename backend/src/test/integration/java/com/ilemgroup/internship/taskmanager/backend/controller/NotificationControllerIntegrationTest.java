package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.ProjectSummary;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.ProjectStatus;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import com.ilemgroup.internship.taskmanager.backend.service.NotificationService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@EnableMethodSecurity
@ContextConfiguration(classes = NotificationController.class)
public class NotificationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void getDetailsList_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "project"
        );
        PageResponse<NotificationDetails> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new NotificationDetails(
                                NotificationType.TICKET_ASSIGNED,
                                new TicketSummary(
                                        1L,
                                        "Ticket",
                                        TicketPriority.MEDIUM,
                                        TicketStatus.IN_TESTING
                                ),
                                false,
                                null
                        )
                )
        );

        when(notificationService.getDetailsList(any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/notification/all")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validQuery)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getDetailsList_invalidQuery_throwsException() throws Exception {
        PageQuery invalidQuery = new PageQuery(
                -1,
                -1,
                null,
                null
        );

        mockMvc.perform(get("/notification/all")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }
}

