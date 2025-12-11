package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.PageQuery;
import com.ilemgroup.internship.taskmanager.backend.dto.PageResponse;
import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.TicketSummary;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketPriority;
import com.ilemgroup.internship.taskmanager.backend.entity.enums.TicketStatus;
import com.ilemgroup.internship.taskmanager.backend.service.TicketService;
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
@ContextConfiguration(classes = TicketController.class)
public class TicketControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Test
    void getSummaryList_validQuery_returnsPage() throws Exception {
        PageQuery validQuery = new PageQuery(
                1,
                10,
                "",
                "ticket"
        );
        PageResponse<TicketSummary> pageResponse = new PageResponse<>(
                1,
                10,
                1,
                1,
                List.of(
                        new TicketSummary(
                                1L,
                                "Ticket",
                                TicketPriority.MEDIUM,
                                TicketStatus.IN_TESTING
                        )
                )
        );

        when(ticketService.getSummaryList(any(PageQuery.class))).thenReturn(pageResponse);

        mockMvc.perform(get("/ticket/summary")
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

        mockMvc.perform(get("/ticket/summary")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidQuery)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTicket_userHasValidRole_success() throws Exception {
        TicketCreate validCommand = new TicketCreate(
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING,
                1L
        );

        mockMvc.perform(post("/ticket/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createTicket_userHasInvalidRole_throwsException() throws Exception {
        TicketCreate validCommand = new TicketCreate(
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING,
                1L
        );

        mockMvc.perform(post("/ticket/create")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTicket_validCommand_success() throws Exception {
        TicketCreate validCommand = new TicketCreate(
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING,
                1L
        );

        mockMvc.perform(post("/ticket/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createTicket_invalidCommand_throwsException() throws Exception {
        TicketCreate invalidCommand = new TicketCreate(
                "",
                "",
                null,
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING,
                1L
        );

        mockMvc.perform(post("/ticket/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTicket_validCommand_success() throws Exception {
        TicketUpdate validCommand = new TicketUpdate(
                1L,
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING
        );

        mockMvc.perform(put("/ticket/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTicket_invalidCommand_throwsException() throws Exception {
        TicketUpdate invalidCommand = new TicketUpdate(
                null,
                "",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING
        );

        mockMvc.perform(put("/ticket/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTicket_userHasValidRole_success() throws Exception {
        TicketUpdate validCommand = new TicketUpdate(
                1L,
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING
        );


        mockMvc.perform(put("/ticket/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTicket_userHasInvalidRole_throwsException() throws Exception {
        TicketUpdate validCommand = new TicketUpdate(
                1L,
                "Ticket",
                "Description",
                "abc123",
                TicketPriority.MEDIUM,
                TicketStatus.IN_TESTING
        );

        mockMvc.perform(put("/ticket/update")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteTicket_userHasValidRole_success() throws Exception {
        mockMvc.perform(delete("/ticket/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTicket_userHasInvalidRole_throwsException() throws Exception {
        mockMvc.perform(delete("/ticket/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("some_guy")))
                .andExpect(status().isForbidden());
    }
}
