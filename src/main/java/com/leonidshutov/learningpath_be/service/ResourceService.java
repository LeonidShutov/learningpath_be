package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;

import java.util.Optional;

public interface ResourceService {
    Resource createResource(Resource resource);

    Optional<Resource> getResourceById(Long id);

    Resource updateResource(Long id, Resource resourceDetails);

    void deleteResource(Long id);
}
