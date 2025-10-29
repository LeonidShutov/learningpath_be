package com.leonidshutov.learningpath_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.ResourceType;
import com.leonidshutov.learningpath_be.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceService resourceService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser(authorities = "ADMIN")
    void createResource_asAdmin_shouldReturnCreated() throws Exception {
        when(resourceService.createResource(any(Resource.class))).thenReturn(resource);

        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Resource"));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void createResource_asUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getResourceById_asAdmin_shouldReturnResource() throws Exception {
        when(resourceService.getResourceById(1L)).thenReturn(Optional.of(resource));

        mockMvc.perform(get("/api/resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateResource_asAdmin_shouldReturnOk() throws Exception {
        when(resourceService.updateResource(eq(1L), any(Resource.class))).thenReturn(resource);

        mockMvc.perform(put("/api/resources/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteResource_asAdmin_shouldReturnNoContent() throws Exception {
        doNothing().when(resourceService).deleteResource(1L);

        mockMvc.perform(delete("/api/resources/1"))
                .andExpect(status().isNoContent());
    }
}
