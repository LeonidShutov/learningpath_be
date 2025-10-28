package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.RegistrationRequest;
import com.leonidshutov.learningpath_be.model.User;
import com.leonidshutov.learningpath_be.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        User newUser = userService.createUser(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword()
        );
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
