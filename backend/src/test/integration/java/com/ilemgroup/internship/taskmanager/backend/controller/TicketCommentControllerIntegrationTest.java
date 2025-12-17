package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.command.create.TicketCommentCreate;
import com.ilemgroup.internship.taskmanager.backend.dto.command.update.TicketCommentUpdate;
import com.ilemgroup.internship.taskmanager.backend.dto.details.TicketCommentDetails;
import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.TicketCommentService;
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

import java.time.LocalDateTime;
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
@ContextConfiguration(classes = TicketCommentController.class)
public class TicketCommentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketCommentService ticketCommentService;

    @Test
    void getSummaryList_validQuery_returnsPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));

        Page<TicketCommentDetails> page = new PageImpl<>(
                List.of(
                        new TicketCommentDetails(
                                1L,
                                new UserSummary(
                                        "abc123",
                                        "John Developer",
                                        "profilePicture.png"
                                ),
                                "Hello!",
                                null,
                                LocalDateTime.now(),
                                null
                        )
                ),
                pageable,
                1
        );

        when(ticketCommentService.getDetailsList(
                any(Long.class),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(get("/ticketComment/all/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
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
        mockMvc.perform(get("/ticketComment/all/1")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/

    @Test
    void createTicketComment_userHasValidRole_success() throws Exception {
        TicketCommentCreate validCommand = new TicketCommentCreate(
                "TicketComment",
                1L,
                null
        );

        mockMvc.perform(post("/ticketComment/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createTicketComment_userHasInvalidRole_throwsException() throws Exception {
        TicketCommentCreate validCommand = new TicketCommentCreate(
                "TicketComment",
                1L,
                null
        );

        mockMvc.perform(post("/ticketComment/create")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createTicketComment_validCommand_success() throws Exception {
        TicketCommentCreate validCommand = new TicketCommentCreate(
                "TicketComment",
                1L,
                null
        );

        mockMvc.perform(post("/ticketComment/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void createTicketComment_invalidCommand_throwsException() throws Exception {
        TicketCommentCreate invalidCommand = new TicketCommentCreate(
                "",
                null,
                null
        );

        mockMvc.perform(post("/ticketComment/create")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTicketComment_validCommand_success() throws Exception {
        TicketCommentUpdate validCommand = new TicketCommentUpdate(
                1L,
                "Comment!"
        );

        mockMvc.perform(put("/ticketComment/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTicketComment_invalidCommand_throwsException() throws Exception {
        TicketCommentUpdate invalidCommand = new TicketCommentUpdate(
                null,
                ""
        );

        mockMvc.perform(put("/ticketComment/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidCommand)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTicketComment_userHasValidRole_success() throws Exception {
        TicketCommentUpdate validCommand = new TicketCommentUpdate(
                1L,
                "Comment!"
        );


        mockMvc.perform(put("/ticketComment/update")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isOk());
    }

    @Test
    void updateTicketComment_userHasInvalidRole_throwsException() throws Exception {
        TicketCommentUpdate validCommand = new TicketCommentUpdate(
                1L,
                "Comment!"
        );

        mockMvc.perform(put("/ticketComment/update")
                        .with(csrf())
                        .with(user("some_guy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validCommand)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteTicketComment_userHasValidRole_success() throws Exception {
        mockMvc.perform(delete("/ticketComment/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTicketComment_userHasInvalidRole_throwsException() throws Exception {
        mockMvc.perform(delete("/ticketComment/delete/{id}", 1L)
                        .with(csrf())
                        .with(user("some_guy")))
                .andExpect(status().isForbidden());
    }
}
