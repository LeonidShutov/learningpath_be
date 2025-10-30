package com.leonidshutov.learningpath_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonidshutov.learningpath_be.dto.StudyPlanRequest;
import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.service.StudyPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

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

    @Test
    @WithMockUser
        // Simulates an authenticated user
    void generateStudyPlan_whenAuthenticatedAndValidRequest_shouldReturn200AndPlan() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest("Java", SkillLevel.BEGINNER, 5);
        List<Resource> mockPlan = Collections.singletonList(Resource.builder().id(1L).title("Java Basics").build());

        when(studyPlanService.generatePlan("Java", SkillLevel.BEGINNER, 5)).thenReturn(mockPlan);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Java Basics"));
    }

    @Test
    void generateStudyPlan_whenNotAuthenticated_shouldReturn401() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest("Java", SkillLevel.BEGINNER, 5);

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void generateStudyPlan_whenTopicIsBlank_shouldReturn400() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest("", SkillLevel.BEGINNER, 5); // Invalid: blank topic

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.topic").value("Topic cannot be empty"));
    }

    @Test
    @WithMockUser
    void generateStudyPlan_whenNumberOfResourcesIsZero_shouldReturn400() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest("Python", SkillLevel.INTERMEDIATE, 0); // Invalid: 0 resources

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.numberOfResources").value("Number of resources must be at least 1"));
    }

    @Test
    @WithMockUser
    void generateStudyPlan_whenSkillLevelIsNull_shouldReturn400() throws Exception {
        // Arrange
        StudyPlanRequest request = new StudyPlanRequest("Go", null, 3); // Invalid: null skill level

        // Act & Assert
        mockMvc.perform(post("/api/study-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.userLevel").value("Skill level cannot be null"));
    }
}