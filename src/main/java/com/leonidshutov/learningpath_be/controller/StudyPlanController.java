package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.StudyPlanRequest;
import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.service.StudyPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing study plan generation.
 * Provides an endpoint for authenticated users to request a personalized study plan.
 */
@RestController
@RequestMapping("/api/study-plans")
@RequiredArgsConstructor // Lombok annotation for constructor injection
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    /**
     * Generates a personalized study plan based on user preferences.
     * This endpoint is secured and requires an authenticated user.
     *
     * @param request The {@link StudyPlanRequest} containing topic, skill level, and number of resources.
     * @return A {@link ResponseEntity} containing a list of {@link Resource} objects representing the generated plan.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Ensures only authenticated users can access this endpoint
    public ResponseEntity<List<Resource>> generateStudyPlan(@Valid @RequestBody StudyPlanRequest request) {
        // The @Valid annotation triggers validation on the StudyPlanRequest DTO.
        // If validation fails, a MethodArgumentNotValidException will be thrown,
        // which Spring Boot typically handles by returning a 400 Bad Request.

        List<Resource> studyPlan = studyPlanService.generatePlan(
                request.getTopic(), request.getUserLevel(), request.getNumberOfResources());
        return ResponseEntity.ok(studyPlan);
    }
}