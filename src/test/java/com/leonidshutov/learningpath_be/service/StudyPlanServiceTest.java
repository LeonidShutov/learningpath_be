package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.model.StudyPlan;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import com.leonidshutov.learningpath_be.repository.StudyPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudyPlanServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private StudyPlanRepository studyPlanRepository;

    @InjectMocks
    private StudyPlanServiceImpl studyPlanService;

    @Test
    void generateAndSavePlan_shouldCreatePlanWithCorrectItems() {
        // given
        User user = new User();
        user.setId(1L);
        String topic = "Java";
        SkillLevel userLevel = SkillLevel.NOVICE;
        int numberOfResources = 2;

        Resource resource1 = new Resource();
        resource1.setId(101L);
        Resource resource2 = new Resource();
        resource2.setId(102L);
        List<Resource> foundResources = List.of(resource1, resource2);

        when(resourceRepository.findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(any(), any()))
                .thenReturn(foundResources);

        ArgumentCaptor<StudyPlan> studyPlanCaptor = ArgumentCaptor.forClass(StudyPlan.class);
        when(studyPlanRepository.save(studyPlanCaptor.capture())).thenAnswer(inv -> {
            StudyPlan capturedPlan = inv.getArgument(0);
            capturedPlan.setId(1L); // Simulate ID being set by persistence
            return capturedPlan;
        });

        // when
        StudyPlan result = studyPlanService.generateAndSavePlan(user, topic, userLevel, numberOfResources);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getPlanItems()).hasSize(2);
        assertThat(result.getPlanItems().get(0).getResource().getId()).isEqualTo(101L);
        assertThat(result.getPlanItems().get(1).getResource().getId()).isEqualTo(102L);

        // Verify the captured StudyPlan before it was saved
        StudyPlan capturedPlan = studyPlanCaptor.getValue();
        assertThat(capturedPlan.getUser()).isEqualTo(user);
        assertThat(capturedPlan.getPlanItems()).hasSize(2);
    }

    @Test
    void generateAndSavePlan_shouldThrowException_whenNumberOfResourcesIsZero() {
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> {
            studyPlanService.generateAndSavePlan(user, "Java", SkillLevel.NOVICE, 0);
        });
    }

    @Test
    void generateAndSavePlan_shouldThrowException_whenNumberOfResourcesIsNegative() {
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> {
            studyPlanService.generateAndSavePlan(user, "Java", SkillLevel.NOVICE, -1);
        });
    }

    @Test
    void generateAndSavePlan_shouldQueryNoviceAndBeginnerResources_forNoviceUser() {
        // given
        User user = new User();
        String topic = "Java";
        SkillLevel userLevel = SkillLevel.NOVICE;
        int numberOfResources = 5;

        when(resourceRepository.findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(any(), any()))
                .thenReturn(List.of()); // Return empty list to avoid NPE
        when(studyPlanRepository.save(any(StudyPlan.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        studyPlanService.generateAndSavePlan(user, topic, userLevel, numberOfResources);

        // then
        ArgumentCaptor<List<SkillLevel>> skillLevelsCaptor = ArgumentCaptor.forClass(List.class);
        verify(resourceRepository).findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(
                eq(topic),
                skillLevelsCaptor.capture()
        );
        assertThat(skillLevelsCaptor.getValue()).containsExactlyInAnyOrder(SkillLevel.NOVICE, SkillLevel.BEGINNER);
    }

    @Test
    void generateAndSavePlan_shouldQueryExpertResources_forExpertUser() {
        // given
        User user = new User();
        String topic = "System Design";
        SkillLevel userLevel = SkillLevel.EXPERT;
        int numberOfResources = 3;

        when(resourceRepository.findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(any(), any()))
                .thenReturn(List.of()); // Return empty list to avoid NPE
        when(studyPlanRepository.save(any(StudyPlan.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        studyPlanService.generateAndSavePlan(user, topic, userLevel, numberOfResources);

        // then
        ArgumentCaptor<List<SkillLevel>> skillLevelsCaptor = ArgumentCaptor.forClass(List.class);
        verify(resourceRepository).findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(
                eq(topic),
                skillLevelsCaptor.capture()
        );
        assertThat(skillLevelsCaptor.getValue()).containsExactly(SkillLevel.EXPERT);
    }
}
