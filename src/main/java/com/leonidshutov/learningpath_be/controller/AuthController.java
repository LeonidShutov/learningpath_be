package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling user authentication-related web pages (UI).
 * This is separate from the REST API controllers.
 */
@Controller
public class AuthController {

    /**
     * Displays the user registration form.
     *
     * @param model The Spring Model to which attributes are added for the view.
     * @return The name of the Thymeleaf template to render ("register").
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Add an empty DTO to the model to bind form data.
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }
}