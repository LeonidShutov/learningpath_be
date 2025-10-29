package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.ResourceType;
import com.leonidshutov.learningpath_be.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private Resource resource;

    @BeforeEach
    void setUp() {
        resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Test Resource");
        resource.setType(ResourceType.ARTICLE);
        resource.setUrl("http://example.com");
    }

    @Test
    void createResource_shouldReturnSavedResource() {
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        Resource created = resourceService.createResource(new Resource());

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Test Resource");
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void getResourceById_shouldReturnResource_whenFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));

        Optional<Resource> found = resourceService.getResourceById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void getResourceById_shouldReturnEmpty_whenNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Resource> found = resourceService.getResourceById(1L);

        assertThat(found).isNotPresent();
    }

    @Test
    void updateResource_shouldReturnUpdatedResource_whenFound() {
        Resource updatedDetails = new Resource();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setType(ResourceType.VIDEO);
        updatedDetails.setUrl("http://new.example.com");

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        Resource updated = resourceService.updateResource(1L, updatedDetails);

        assertThat(updated.getTitle()).isEqualTo("Updated Title");
        assertThat(updated.getType()).isEqualTo(ResourceType.VIDEO);
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    void updateResource_shouldThrowException_whenNotFound() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            resourceService.updateResource(1L, new Resource());
        });
    }

    @Test
    void deleteResource_shouldCompleteSuccessfully_whenFound() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRepository).deleteById(1L);

        resourceService.deleteResource(1L);

        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteResource_shouldThrowException_whenNotFound() {
        when(resourceRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            resourceService.deleteResource(1L);
        });
    }
}
