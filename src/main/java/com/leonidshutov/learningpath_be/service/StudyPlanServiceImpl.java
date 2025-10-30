package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyPlanServiceImpl implements StudyPlanService {

    private final ResourceRepository resourceRepository;

    /**
     * Generates a study plan.
     * <p>
     * In a next steps, this method will implement the "Hybrid Intelligence Model":
     * 1. Fetch relevant resources from the database using the repository based on topic and skill level.
     * 2. Construct a detailed prompt for the AI (e.g., OpenAI GPT-4-Turbo).
     * 3. Send the curated list of resources and user profile to the AI service.
     * 4. The AI would then select, sequence, and pace the resources, returning a structured JSON plan.
     * 5. This service would parse the AI's response and return the final list of resources.
     * <p>
     * For now, we simulate this by fetching resources and taking the requested number.
     */
    @Override
    public List<Resource> generatePlan(String topic, SkillLevel userLevel, int numberOfResources) {
        if (numberOfResources <= 0) {
            throw new IllegalArgumentException("Number of resources must be positive.");
        }

        // We need to fetch resources that are at or below the user's skill level.
        List<SkillLevel> relevantSkillLevels = new ArrayList<>();
        // Add all skill levels up to and including the user's current level
        for (SkillLevel level : SkillLevel.values()) {
            if (level == userLevel) {
                relevantSkillLevels.add(level);
                break;
            }
            relevantSkillLevels.add(level);
        }

        List<Resource> potentialResources = resourceRepository.findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(topic, relevantSkillLevels);

        return potentialResources.stream().limit(numberOfResources).collect(Collectors.toList());
    }
}