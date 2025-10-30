package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.SkillLevel;
import com.leonidshutov.learningpath_be.model.StudyPlan;
import com.leonidshutov.learningpath_be.model.User;

public interface StudyPlanService {
    StudyPlan generateAndSavePlan(User user, String topic, SkillLevel userLevel, int numberOfResources);
}
