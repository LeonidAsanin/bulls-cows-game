package org.leonidasanin.bullscowsgame.controller;

import org.leonidasanin.bullscowsgame.exception.NotEnoughDigitsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling custom exceptions.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NotEnoughDigitsException.class)
    public String handleNotEnoughDigitsException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Not Enough Digits (Must be 4)");
        return "redirect:/";
    }
}
