package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.RegistrationRequest;
import com.leonidshutov.learningpath_be.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void showRegistrationForm_shouldReturnRegisterViewWithRegistrationRequest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationRequest"))
                .andExpect(model().attribute("registrationRequest", instanceOf(RegistrationRequest.class)));
    }

    @Test
    void login_withValidCredentials_shouldRedirectToHome() throws Exception {
        // Arrange
        String username = "user";
        String password = "password";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                passwordEncoder.encode(password),
                Collections.emptyList()
        );
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act & Assert
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf())) // Include CSRF token for web form submission
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void login_withInvalidCredentials_shouldRedirectToLoginWithError() throws Exception {
        // Arrange
        String username = "user";
        String password = "wrongpassword";
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                username,
                passwordEncoder.encode("password"),
                Collections.emptyList()
        );
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act & Assert
        mockMvc.perform(post("/login")
                        .param("username", username)
                        .param("password", password)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
