package com.leonidshutov.learningpath_be.service;

import com.leonidshutov.learningpath_be.model.User;

public interface UserService {
    User createUser(String username, String email, String password);
}
