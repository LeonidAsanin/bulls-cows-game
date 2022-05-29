package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller that gives custom handling of "get" http-method request to "/login".
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", "Invalid Credentials");
        model.addAttribute("user", new User());
        return "login";
    }
}
