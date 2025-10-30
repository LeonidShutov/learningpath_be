package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.StudyPlanRequest;
import com.leonidshutov.learningpath_be.model.StudyPlan;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.service.StudyPlanService;
import com.leonidshutov.learningpath_be.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/study-plans")
@AllArgsConstructor
public class StudyPlanController {

    private final StudyPlanService studyPlanService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<StudyPlan> generateStudyPlan(
            @Valid @RequestBody StudyPlanRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        StudyPlan studyPlan = studyPlanService.generateAndSavePlan(
                user,
                request.getTopic(),
                request.getUserLevel(),
                request.getNumberOfResources()
        );

        return new ResponseEntity<>(studyPlan, HttpStatus.CREATED);
    }
}
