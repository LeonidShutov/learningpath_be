package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    public Resource createResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public Optional<Resource> getResourceById(Long id) {
        return resourceRepository.findById(id);
    }

    @Override
    public Resource updateResource(Long id, Resource resourceDetails) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Resource not found with id: " + id));

        resource.setTitle(resourceDetails.getTitle());
        resource.setType(resourceDetails.getType());
        resource.setUrl(resourceDetails.getUrl());
        resource.setEstimatedTimeMinutes(resourceDetails.getEstimatedTimeMinutes());
        resource.setBaseEffectivenessScore(resourceDetails.getBaseEffectivenessScore());
        resource.setTopicTags(resourceDetails.getTopicTags());
        resource.setPrerequisites(resourceDetails.getPrerequisites());

        return resourceRepository.save(resource);
    }

    @Override
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new IllegalArgumentException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
    }
}
