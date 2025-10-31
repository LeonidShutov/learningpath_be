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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @WithMockUser(username = "testuser")
    void getStudyPlansForUser_shouldReturnListOfPlans() throws Exception {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        StudyPlan plan1 = new StudyPlan();
        plan1.setId(1L);
        StudyPlan plan2 = new StudyPlan();
        plan2.setId(2L);
        List<StudyPlan> userPlans = List.of(plan1, plan2);

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(studyPlanService.getPlansForUser(mockUser)).thenReturn(userPlans);

        // Act & Assert
        mockMvc.perform(get("/api/study-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @WithMockUser(username = "testuser")
    void getStudyPlansForUser_whenNoPlansExist_shouldReturnEmptyList() throws Exception {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(studyPlanService.getPlansForUser(mockUser)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/study-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void getStudyPlansForUser_whenNotAuthenticated_shouldReturnUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/study-plans"))
                .andExpect(status().isUnauthorized());
    }
}
