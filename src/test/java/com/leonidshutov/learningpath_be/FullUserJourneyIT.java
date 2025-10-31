package com.leonidshutov.learningpath_be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonidshutov.learningpath_be.dto.RegistrationRequest;
import com.leonidshutov.learningpath_be.dto.StudyPlanRequest;
import com.leonidshutov.learningpath_be.model.*;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import com.leonidshutov.learningpath_be.repository.StudyPlanRepository;
import com.leonidshutov.learningpath_be.repository.UserProfileRepository;
import com.leonidshutov.learningpath_be.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FullUserJourneyIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    @BeforeEach
    void setUp() {
        studyPlanRepository.deleteAll();
        userProfileRepository.deleteAll();
        userRepository.deleteAll();
        resourceRepository.deleteAll();
    }

    @Test
    void testFullUserJourney() throws Exception {
        // Step 1: Register a new user
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("journeyuser");
        registrationRequest.setEmail("journey@example.com");
        registrationRequest.setPassword("password123");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        // Verify user and profile were created in the database
        User user = userRepository.findByUsername("journeyuser").orElseThrow();
        UserProfile userProfile = userProfileRepository.findById(user.getId()).orElseThrow();
        assertThat(userProfile.getAlgorithmsLevel()).isEqualTo(SkillLevel.NOVICE);

        // Step 2: Log in as the new user
        mockMvc.perform(post("/api/users/login")
                        .param("username", "journeyuser")
                        .param("password", "password123"))
                .andExpect(status().isOk());

        // Step 3: Prepare resources for the study plan
        Resource resource1 = new Resource();
        resource1.setTitle("Java Concurrency");
        resource1.setType(ResourceType.ARTICLE);
        resource1.setUrl("http://example.com/jc");
        resource1.setSkillLevel(SkillLevel.BEGINNER);
        resource1.setTopicTags(List.of("Java"));
        resourceRepository.save(resource1);

        // Step 4: Generate and save a study plan
        StudyPlanRequest studyPlanRequest = new StudyPlanRequest();
        studyPlanRequest.setTopic("Java");
        studyPlanRequest.setUserLevel(SkillLevel.NOVICE);
        studyPlanRequest.setNumberOfResources(1);

        mockMvc.perform(post("/api/study-plans")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("journeyuser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studyPlanRequest)))
                .andExpect(status().isCreated());

        // Step 5: Verify the study plan was persisted correctly
        List<StudyPlan> plans = studyPlanRepository.findAll();
        assertThat(plans).hasSize(1);
        StudyPlan savedPlan = plans.getFirst();
        assertThat(savedPlan.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedPlan.getPlanItems()).hasSize(1);
        assertThat(savedPlan.getPlanItems().getFirst().getResource().getTitle()).isEqualTo("Java Concurrency");
    }
}
