package com.ilemgroup.internship.taskmanager.backend.security;

import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMethodSecurity
public class TemporaryAuthenticationFilterTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @Test
    void anonymousAuthentication_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/secure")
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(status().isOk());
    }

    @Test
    void validAuthenticationWithInsufficientPermissions_shouldReturn403() throws Exception {
        mockMvc.perform(get("/secure").with(
                        SecurityMockMvcRequestPostProcessors.user("guy").roles("TEAM_BEMBER")
                ))
                .andExpect(status().is4xxClientError());
    }
}
