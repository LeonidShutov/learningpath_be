package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.*;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import com.leonidshutov.learningpath_be.repository.StudyPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudyPlanServiceImpl implements StudyPlanService {

    private final ResourceRepository resourceRepository;
    private final StudyPlanRepository studyPlanRepository;

    @Override
    @Transactional
    public StudyPlan generateAndSavePlan(User user, String topic, SkillLevel userLevel, int numberOfResources) {
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
        List<Resource> resources = resourceRepository.findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(topic, targetSkillLevels);

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setUser(user);

        List<PlanItem> planItems = resources.stream()
                .map(resource -> {
                    PlanItem planItem = new PlanItem();
                    planItem.setResource(resource);
                    // Simple scheduling logic for now: all in week 1, day 1
                    planItem.setWeek(1);
                    planItem.setDayOfWeek(1);
                    return planItem;
                })
                .collect(Collectors.toList());

        studyPlan.setPlanItems(planItems);

        return studyPlanRepository.save(studyPlan);
    }
}
