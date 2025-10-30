package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.User;

import java.util.Optional;

public interface UserService {
    User createUser(String username, String email, String password);

    Optional<User> findByUsername(String username);
}
