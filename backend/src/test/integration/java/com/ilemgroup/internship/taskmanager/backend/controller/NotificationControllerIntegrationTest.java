package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.details.NotificationDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.NotificationType;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import com.ilemgroup.internship.taskmanager.backend.service.NotificationService;
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
@ContextConfiguration(classes = NotificationController.class)
public class NotificationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void getDetailsList_validQuery_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NotificationDetails> page = new PageImpl<>(
                List.of(
                        new NotificationDetails(
                                1L,
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
                ),
                pageable,
                1
        );

        when(notificationService.getAllForClient(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/notification/details")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }


    /*@Test
    void getDetailsList_invalidQuery_throwsException() throws Exception {
        mockMvc.perform(get("/notification/all")
                        .with(csrf())
                        .with(user("project_manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/
}

