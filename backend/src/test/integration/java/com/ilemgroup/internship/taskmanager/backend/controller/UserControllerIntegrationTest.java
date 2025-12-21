package com.ilemgroup.internship.taskmanager.backend.controller;

import com.ilemgroup.internship.taskmanager.backend.dto.summary.UserSummary;
import com.ilemgroup.internship.taskmanager.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

        when(userService.getAllUsers(
                any(Pageable.class),
                any(),
                any()
        )).thenReturn(page);

        mockMvc.perform(get("/user/summary")
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
        mockMvc.perform(get("/user/summary")
                        .with(csrf())
                        .with(user("manager").roles("PROJECT_MANAGER"))
                        .param("page", "-1")
                        .param("size", "-10"))
                .andExpect(status().isBadRequest());
    }*/
}
