package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudyPlanServiceImpl implements StudyPlanService {

    private final ResourceRepository resourceRepository;

    @Override
    public List<Resource> generatePlan(String topic, SkillLevel userLevel, int numberOfResources) {
        if (numberOfResources <= 0) {
            throw new IllegalArgumentException("Number of resources must be positive.");
        }

        List<SkillLevel> targetSkillLevels = switch (userLevel) {
            case NOVICE -> List.of(SkillLevel.NOVICE, SkillLevel.BEGINNER);
            case BEGINNER -> List.of(SkillLevel.BEGINNER, SkillLevel.INTERMEDIATE);
            case INTERMEDIATE -> List.of(SkillLevel.INTERMEDIATE, SkillLevel.ADVANCED);
            case ADVANCED -> List.of(SkillLevel.ADVANCED, SkillLevel.EXPERT);
            case EXPERT -> List.of(SkillLevel.EXPERT);
        };

        PageRequest pageRequest = PageRequest.of(0, numberOfResources);
        return resourceRepository.findByTopicTagsContainingAndSkillLevelIn(topic, targetSkillLevels, pageRequest);
    }
}
