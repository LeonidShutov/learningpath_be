package com.leonidshutov.learningpath_be.repository;

import com.leonidshutov.learningpath_be.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByUsername(user.getUsername());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void whenFindByEmail_thenReturnUser() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> found = userRepository.findByEmail(user.getEmail());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }
}