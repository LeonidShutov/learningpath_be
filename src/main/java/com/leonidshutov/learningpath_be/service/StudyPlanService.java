package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;

import java.util.List;

public interface StudyPlanService {
    List<Resource> generatePlan(String topic, SkillLevel userLevel, int numberOfResources);
}
