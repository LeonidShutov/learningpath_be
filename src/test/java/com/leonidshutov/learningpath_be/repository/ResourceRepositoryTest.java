package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.Resource;
import com.leonidshutov.learningpath_be.model.ResourceType;
import com.leonidshutov.learningpath_be.model.SkillLevel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ResourceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResourceRepository resourceRepository;

    @Test
    public void whenFindById_thenReturnResource() {
        // given
        Resource resource = new Resource();
        resource.setTitle("Test Resource");
        resource.setType(ResourceType.ARTICLE);
        resource.setUrl("http://test.com");
        resource.setSkillLevel(SkillLevel.NOVICE); // Set the mandatory skillLevel
        entityManager.persist(resource);
        entityManager.flush();

        // when
        Resource found = resourceRepository.findById(resource.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo(resource.getTitle());
    }
}
