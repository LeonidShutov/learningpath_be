package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.*;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import com.leonidshutov.learningpath_be.repository.StudyPlanRepository;
import com.leonidshutov.learningpath_be.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class StudyPlanPersistenceTest {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StudyPlanRepository studyPlanRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        resourceRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRoles(Set.of(Role.USER));
        userRepository.save(testUser);

        Resource resource1 = new Resource();
        resource1.setTitle("Java Basics");
        resource1.setType(ResourceType.ARTICLE);
        resource1.setUrl("http://example.com/java1");
        resource1.setSkillLevel(SkillLevel.NOVICE);
        resource1.setTopicTags(List.of("Java"));
        resourceRepository.save(resource1);

        Resource resource2 = new Resource();
        resource2.setTitle("Java Advanced");
        resource2.setType(ResourceType.VIDEO);
        resource2.setUrl("http://example.com/java2");
        resource2.setSkillLevel(SkillLevel.BEGINNER);
        resource2.setTopicTags(List.of("Java"));
        resourceRepository.save(resource2);
    }

    @Test
    void generateAndSavePlan_shouldPersistPlanAndItems() {
        // when
        StudyPlan savedPlan = studyPlanService.generateAndSavePlan(testUser, "Java", SkillLevel.NOVICE, 2);

        // then
        assertThat(savedPlan.getId()).isNotNull();

        StudyPlan foundPlan = studyPlanRepository.findById(savedPlan.getId()).orElse(null);
        assertThat(foundPlan).isNotNull();
        assertThat(foundPlan.getUser().getId()).isEqualTo(testUser.getId());
        assertThat(foundPlan.getPlanItems()).hasSize(2);
        assertThat(foundPlan.getPlanItems().get(0).getResource()).isNotNull();
        assertThat(foundPlan.getPlanItems().get(0).getWeek()).isEqualTo(1);
    }
}
