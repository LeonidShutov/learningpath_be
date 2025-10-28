package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
