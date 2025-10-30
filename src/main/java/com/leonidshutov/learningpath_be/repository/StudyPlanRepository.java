package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.StudyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
}
