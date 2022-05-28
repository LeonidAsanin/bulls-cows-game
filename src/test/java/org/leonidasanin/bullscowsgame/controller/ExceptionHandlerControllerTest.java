package org.leonidasanin.bullscowsgame.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {
    @Mock
    RedirectAttributes redirectAttributes;

    @Test
    void handleNotEnoughDigitsException() {
        //given
        ExceptionHandlerController exceptionHandlerController = new ExceptionHandlerController();

        //when
        String result = exceptionHandlerController.handleNotEnoughDigitsException(redirectAttributes);

        //then
        assertEquals("redirect:/", result);
        Mockito.verify(redirectAttributes)
                .addFlashAttribute("error", "Not Enough Digits (Must be 4)");
    }
}