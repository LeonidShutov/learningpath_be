package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByTopicTagsContainingAndSkillLevelIn(String topic, List<SkillLevel> skillLevels, Pageable pageable);
}
