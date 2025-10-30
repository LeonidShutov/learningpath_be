package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

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

    @InjectMocks
    private StudyPlanServiceImpl studyPlanService;

    @Test
    void generatePlan_shouldQueryNoviceAndBeginnerResources_forNoviceUser() {
        // given
        String topic = "Java";
        SkillLevel userLevel = SkillLevel.NOVICE;
        int numberOfResources = 5;

        // when
        studyPlanService.generatePlan(topic, userLevel, numberOfResources);

        // then
        ArgumentCaptor<List<SkillLevel>> skillLevelsCaptor = ArgumentCaptor.forClass(List.class);
        verify(resourceRepository).findByTopicTagsContainingAndSkillLevelIn(
                eq(topic),
                skillLevelsCaptor.capture(),
                any(PageRequest.class)
        );
        assertThat(skillLevelsCaptor.getValue()).containsExactlyInAnyOrder(SkillLevel.NOVICE, SkillLevel.BEGINNER);
    }

    @Test
    void generatePlan_shouldQueryExpertResources_forExpertUser() {
        // given
        String topic = "System Design";
        SkillLevel userLevel = SkillLevel.EXPERT;
        int numberOfResources = 3;

        // when
        studyPlanService.generatePlan(topic, userLevel, numberOfResources);

        // then
        ArgumentCaptor<List<SkillLevel>> skillLevelsCaptor = ArgumentCaptor.forClass(List.class);
        verify(resourceRepository).findByTopicTagsContainingAndSkillLevelIn(
                eq(topic),
                skillLevelsCaptor.capture(),
                any(PageRequest.class)
        );
        assertThat(skillLevelsCaptor.getValue()).containsExactly(SkillLevel.EXPERT);
    }

    @Test
    void generatePlan_shouldReturnCorrectResources() {
        // given
        String topic = "Java";
        SkillLevel userLevel = SkillLevel.BEGINNER;
        int numberOfResources = 2;
        List<Resource> expectedResources = List.of(new Resource(), new Resource());

        when(resourceRepository.findByTopicTagsContainingAndSkillLevelIn(any(), any(), any()))
                .thenReturn(expectedResources);

        // when
        List<Resource> actualResources = studyPlanService.generatePlan(topic, userLevel, numberOfResources);

        // then
        assertThat(actualResources).isEqualTo(expectedResources);
    }

    @Test
    void generatePlan_shouldThrowException_whenNumberOfResourcesIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            studyPlanService.generatePlan("Java", SkillLevel.NOVICE, 0);
        });
    }
}
