package com.leonidshutov.learningpath_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonidshutov.learningpath_be.dto.StudyPlanRequest;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.model.StudyPlan;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.service.StudyPlanService;
import com.leonidshutov.learningpath_be.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudyPlanControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudyPlanService studyPlanService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testuser")
    void generateStudyPlan_whenAuthenticatedAndValidRequest_shouldReturnCreatedAndPlan() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic("Java");
        request.setUserLevel(SkillLevel.BEGINNER);
        request.setNumberOfResources(5);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        StudyPlan mockStudyPlan = new StudyPlan();
        mockStudyPlan.setId(10L);
        mockStudyPlan.setUser(mockUser);

        when(userService.findByUsername(anyString())).thenReturn(Optional.of(mockUser));
        when(studyPlanService.generateAndSavePlan(any(User.class), anyString(), any(SkillLevel.class), anyInt()))
                .thenReturn(mockStudyPlan);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }

    @Test
    void generateStudyPlan_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic("Java");
        request.setUserLevel(SkillLevel.BEGINNER);
        request.setNumberOfResources(5);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testuser")
    void generateStudyPlan_whenTopicIsBlank_shouldReturnBadRequest() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic(""); // Invalid: blank topic
        request.setUserLevel(SkillLevel.BEGINNER);
        request.setNumberOfResources(5);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void generateStudyPlan_whenNumberOfResourcesIsZero_shouldReturnBadRequest() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic("Python");
        request.setUserLevel(SkillLevel.INTERMEDIATE);
        request.setNumberOfResources(0); // Invalid: 0 resources

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser")
    void generateStudyPlan_whenSkillLevelIsNull_shouldReturnBadRequest() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest();
        request.setTopic("Go");
        request.setUserLevel(null); // Invalid: null skill level
        request.setNumberOfResources(3);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
