package com.leonidshutov.learningpath_be.dto;

import com.leonidshutov.learningpath_be.model.SkillLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting a new study plan.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyPlanRequest {

    @NotBlank(message = "Topic cannot be empty")
    private String topic;

    @NotNull(message = "Skill level cannot be null")
    private SkillLevel userLevel;

    @Min(value = 1, message = "Number of resources must be at least 1")
    private int numberOfResources;
}