package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Optional;

@Controller
@SessionScope
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String getPage(Model model) {
        Optional<Integer> optionalUserNumber = gameService.getNumber();
        optionalUserNumber.ifPresent(number -> model.addAttribute("userNumber", number));

        return "game";
    }

    @PostMapping("/enter/{digit}")
    public String enterDigit(@PathVariable(name = "digit") int digit) {
        gameService.addDigit(digit);
        return "redirect:/game";
    }

    @PostMapping("/delete")
    public String deleteDigit() {
        gameService.deleteDigit();
        return "redirect:/game";
    }

    @PostMapping("/try")
    public String tryNumber() {
        gameService.tryNumber();
        return "redirect:/game";
    }
}
