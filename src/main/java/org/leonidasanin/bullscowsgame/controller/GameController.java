package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.exception.NotEnoughDigitsException;
import org.leonidasanin.bullscowsgame.service.GameService;
import org.leonidasanin.bullscowsgame.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;


@Controller
@SessionScope
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping
    public String getPage(Model model, @AuthenticationPrincipal User user) {
        double averageAttemptNumberToWin = userService.getAverageAttemptNumberToWinByUserId(user.getId());
        model.addAttribute("averageAttemptNumberToWin", averageAttemptNumberToWin);

        String userNumber = gameService.getNumber();
        model.addAttribute("userNumber", userNumber);

        model.addAttribute("result", gameService.getResult());

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
    public String tryNumber() throws NotEnoughDigitsException {
        gameService.tryNumber();
        return "redirect:/game";
    }
}
