package com.leonidshutov.learningpath_be.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonidshutov.learningpath_be.dto.RegistrationRequest;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRoles(Set.of());
        userRepository.save(user);
    }

    @Test
    void login_shouldReturnOk_whenCredentialsAreValid() throws Exception {
        mockMvc.perform(post("/api/users/login")
                        .param("username", "testuser")
                        .param("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    void login_shouldReturnUnauthorized_whenPasswordIsInvalid() throws Exception {
        mockMvc.perform(post("/api/users/login")
                        .param("username", "testuser")
                        .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturnUnauthorized_whenUsernameDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/users/login")
                        .param("username", "nonexistentuser")
                        .param("password", "password123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void fullRegistrationAndLoginFlow_shouldSucceed() throws Exception {
        // Step 1: Attempt to log in with a new user and fail
        String username = "newbie";
        String password = "password12345";
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized());

        // Step 2: Register the new user
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername(username);
        registrationRequest.setEmail("newbie@example.com");
        registrationRequest.setPassword(password);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // Step 3: Attempt to log in again and succeed
        mockMvc.perform(post("/api/users/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk());
    }
}
