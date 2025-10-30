package com.leonidshutov.learningpath_be.dto;

import com.leonidshutov.learningpath_be.model.SkillLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudyPlanRequest {

    @NotEmpty
    private String topic;

    @NotNull
    private SkillLevel userLevel;

    @Min(1)
    private int numberOfResources;
}
