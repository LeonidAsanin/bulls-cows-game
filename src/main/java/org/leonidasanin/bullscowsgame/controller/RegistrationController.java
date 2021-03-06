package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that implements registration logic.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getRegisterPage() {
        return "registration";
    }

    @PostMapping
    public String registerNewUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String passwordConfirmation,
            Model model
    ) {
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Sorry, but this username already exists");
            return "registration";
        }
        if (!password.equals(passwordConfirmation)) {
            model.addAttribute("error", "Please, type in your desired password twice correctly");
            model.addAttribute("username", username);
            return "registration";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userService.registerNewUser(user);

        return "redirect:/login";
    }
}
