package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, Long> {
}
