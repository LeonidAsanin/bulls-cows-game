package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.exception.NotEnoughDigitsException;
import org.leonidasanin.bullscowsgame.model.GameResult;
import org.leonidasanin.bullscowsgame.service.GameService;
import org.leonidasanin.bullscowsgame.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final UserService userService;

    private String result;

    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping
    public String getPage(Model model, @AuthenticationPrincipal User user) {
        String userNumber = gameService.getNumber();
        model.addAttribute("userNumber", userNumber);

        double averageAttemptNumberToWin = userService.getAverageAttemptNumberToWinByUserId(user.getId());
        model.addAttribute("averageAttemptNumberToWin", averageAttemptNumberToWin);

        model.addAttribute("result", result);

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
        StringBuilder resultBuilder = new StringBuilder();

        GameResult gameResult = gameService.tryNumber();
        if (gameResult.isSuccess()) {
            resultBuilder.append("You won! Secret number is ")
                    .append(gameResult.getSecretNumber())
                    .append(". Attempts: ")
                    .append(gameResult.getAttemptBullsAndCowsList().size())
                    .append("<br/> Try to guess new one!");
        } else {
            resultBuilder.append("Previous tries: <br/>");

            int tryCounter = 1;
            for (String attempt : gameResult.getAttemptBullsAndCowsList()) {
                resultBuilder.append("[")
                        .append(tryCounter++)
                        .append("] ")
                        .append(attempt)
                        .append("<br/>");
            }
        }

        result = resultBuilder.toString();

        return "redirect:/game";
    }
}
