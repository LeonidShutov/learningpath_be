package com.leonidshutov.learningpath_be.controller;

import com.leonidshutov.learningpath_be.dto.RegistrationRequest;
import com.leonidshutov.learningpath_be.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationRequest") RegistrationRequest registrationRequest,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(
                    registrationRequest.getUsername(),
                    registrationRequest.getEmail(),
                    registrationRequest.getPassword()
            );
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "email.exists", "Email already exists");
            return "register";
        }

        return "redirect:/login?registrationSuccess";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
