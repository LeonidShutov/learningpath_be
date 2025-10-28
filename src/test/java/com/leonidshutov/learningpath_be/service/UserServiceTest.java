package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.Role;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@test.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.USER));
    }

    @Test
    void createUser_shouldReturnUser_whenUsernameAndEmailAreUnique() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("testuser@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser("testuser", "testuser@test.com", "password");

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("testuser");
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("testuser", "newemail@test.com", "password");
        });
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("testuser@test.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser("newuser", "testuser@test.com", "password");
        });
    }
}
