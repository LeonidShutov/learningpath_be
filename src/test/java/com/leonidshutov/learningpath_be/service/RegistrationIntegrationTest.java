package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.model.UserProfile;
import com.leonidshutov.learningpath_be.repository.UserProfileRepository;
import com.leonidshutov.learningpath_be.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class RegistrationIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void whenUserIsCreated_thenUserProfileIsAlsoCreatedAndLinked() {
        // given
        String username = "newuser";
        String email = "newuser@example.com";
        String password = "password123";

        // when
        User createdUser = userService.createUser(username, email, password);

        // then
        assertThat(createdUser.getId()).isNotNull();

        // Verify User and UserProfile are in their respective repositories
        User foundUser = userRepository.findById(createdUser.getId()).orElse(null);
        UserProfile foundProfile = userProfileRepository.findById(createdUser.getId()).orElse(null);

        assertThat(foundUser).isNotNull();
        assertThat(foundProfile).isNotNull();

        // Verify the link between User and UserProfile
        assertThat(foundUser.getUserProfile()).isNotNull();
        assertThat(foundUser.getUserProfile().getId()).isEqualTo(foundUser.getId());
        assertThat(foundProfile.getUser().getId()).isEqualTo(foundUser.getId());
    }
}
