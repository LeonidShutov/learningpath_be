package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    /**
     * Finds resources by topic tags (case-insensitive) and a collection of skill levels.
     * This allows fetching resources relevant to a user's skill level (e.g., BEGINNER and NOVICE resources for a BEGINNER user).
     *
     * @param topicTag    The topic tag to search for.
     * @param skillLevels A collection of skill levels to include in the search.
     * @return A list of matching resources.
     */
    List<Resource> findByTopicTagsContainingIgnoreCaseAndSkillLevelIn(String topicTag, Collection<SkillLevel> skillLevels);
}