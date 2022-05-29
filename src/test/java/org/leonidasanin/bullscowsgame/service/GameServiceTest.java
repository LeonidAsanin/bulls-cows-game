package org.leonidasanin.bullscowsgame.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.leonidasanin.bullscowsgame.controller.AuthenticationForTestOfControllers;
import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.exception.NotEnoughDigitsException;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    GameService gameService;

    @Mock
    UserRepository userRepositoryMock;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setPassword("password");

        AuthenticationForTestOfControllers authentication = new AuthenticationForTestOfControllers(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        gameService = new GameService(userRepositoryMock);
    }

    @Test
    void addDigit() {
        //given
        int[] digits = {0, 1, 2, 2, 3, 4};
        String[] expectedNumbers = {"0", "01", "012", "012", "0123", "0123"};

        //when
        String[] resultNumbers = new String[expectedNumbers.length];
        for (int i = 0; i < digits.length; i++) {
            gameService.addDigit(digits[i]);
            resultNumbers[i] = gameService.getNumber();
        }

        //then
        for (int i = 0; i < resultNumbers.length; i++) {
            assertEquals(expectedNumbers[i], resultNumbers[i]);
        }
    }

    @Test
    void deleteDigit() {
        //given
        int[] digits = {0, 9, 1, 8};
        for (int digit : digits) {
            gameService.addDigit(digit);
        }
        String[] expectedNumbers = {"091", "09", "0", "", ""};

        //when
        String[] resultNumbers = new String[expectedNumbers.length];
        for (int i = 0; i < resultNumbers.length; i++) {
            gameService.deleteDigit();
            resultNumbers[i] = gameService.getNumber();
        }

        //then
        for (int i = 0; i < resultNumbers.length; i++) {
            assertEquals(expectedNumbers[i], resultNumbers[i]);
        }
    }

    @Test
    void getNumber() {
        //given
        int[] digits = {0, 7, 3, 9};
        for (int digit : digits) {
            gameService.addDigit(digit);
        }
        String expectedNumber = "0739";

        //when
        String resultNumber = gameService.getNumber();

        //then
        assertEquals(expectedNumber, resultNumber);
    }

    @Test
    void getSecretNumber() {
        //given
        int length = 4;

        //when
        Set<Integer> resultDigitSet = new HashSet<>();
        String secretNumber = gameService.getSecretNumber();
        for (int i = 0; i < secretNumber.length(); i++) {
            resultDigitSet.add(Integer.parseInt("" + secretNumber.charAt(i)));
        }

        //then
        assertEquals(length, secretNumber.length());
        assertEquals(length, resultDigitSet.size());
        for (int digit : resultDigitSet) {
            assertTrue(digit >= 0 && digit < 10);
        }
    }

    @Test
    void setSecretNumber() {
        //given
        LinkedList<Integer> secretNumberOK = new LinkedList<>();
        LinkedList<Integer> secretNumberNotEnoughDigits = new LinkedList<>();
        LinkedList<Integer> secretNumberRepeatedDigits = new LinkedList<>();
        Collections.addAll(secretNumberOK, 0, 1, 8, 9);
        Collections.addAll(secretNumberNotEnoughDigits, 0, 1, 8);
        Collections.addAll(secretNumberRepeatedDigits, 0, 1, 8, 8);

        //when
        gameService.setSecretNumber(secretNumberOK);
        String result = gameService.getSecretNumber();
        Executable throwing1 = () -> gameService.setSecretNumber(secretNumberNotEnoughDigits);
        Executable throwing2 = () -> gameService.setSecretNumber(secretNumberRepeatedDigits);

        //then
        assertEquals("0189", result);
        assertThrows(NumberFormatException.class, throwing1);
        assertThrows(NumberFormatException.class, throwing2);
    }

    @Test
    void tryNumberAndGetSuccessResult() throws NotEnoughDigitsException {
        //given
        String secretNumber = gameService.getSecretNumber();
        for (int i = 0; i < secretNumber.length(); i++) {
            gameService.addDigit(Integer.parseInt("" + secretNumber.charAt(i)));
        }
        String expectedResult = "You won! Secret number is " + secretNumber
                + ". Attempts: 1"
                + "<br/> Try to guess new one!";

        //when
        gameService.tryNumber();
        String result = gameService.getResult();

        //then
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0182_30"/*3(bulls); 0(cows)*/,
            "1098_04"/*0(bulls); 4(cows)*/,
            "9182_21"/*2(bulls); 1(cows)*/
            }
    )
    void tryNumberAndGetUnsuccessfulResult(String attempt) throws NotEnoughDigitsException {
        //given
        LinkedList<Integer> secretNumber = new LinkedList<>();
        Collections.addAll(secretNumber, 0, 1, 8, 9);
        gameService.setSecretNumber(secretNumber);
        for (int i = 0; i < 4; i++) {
            int digit = Integer.parseInt("" + attempt.charAt(i));
            gameService.addDigit(digit);
        }
        String expectedResult = "Previous tries: <br/>[1] " + attempt.substring(0, 4) +
                " [" + attempt.charAt(5) + "(bulls) and " + attempt.charAt(6) + "(cows)]<br/>";

        //when
        gameService.tryNumber();
        String result = gameService.getResult();

        //then
        assertEquals(expectedResult, result);
    }

    @Test
    void trySeveralNumbersUnsuccessfullyAndFinallyWin() throws NotEnoughDigitsException {
        //given
        LinkedList<Integer> secretNumber = new LinkedList<>();
        Collections.addAll(secretNumber, 7, 3, 2, 8);
        gameService.setSecretNumber(secretNumber);

        String expectedUnsuccessfulResult = "Previous tries: <br/>" +
                "[1] 0819 [0(bulls) and 1(cows)]<br/>" +
                "[2] 4073 [0(bulls) and 2(cows)]<br/>" +
                "[3] 5820 [1(bulls) and 1(cows)]<br/>" +
                "[4] 3429 [1(bulls) and 1(cows)]<br/>" +
                "[5] 5960 [0(bulls) and 0(cows)]<br/>" +
                "[6] 7238 [2(bulls) and 2(cows)]<br/>";
        String expectedSuccessfulResult = "You won! Secret number is 7328. Attempts: 7" +
                "<br/> Try to guess new one!";

        //when
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                int digit = Integer.parseInt("" + expectedUnsuccessfulResult.charAt(25 + j + 36 * i));
                gameService.addDigit(digit);
            }
            gameService.tryNumber();
        }
        String unsuccessfulResult = gameService.getResult();
        gameService.addDigit(7);
        gameService.addDigit(3);
        gameService.addDigit(2);
        gameService.addDigit(8);
        gameService.tryNumber();
        String successfulResult = gameService.getResult();

        //then
        assertEquals(expectedUnsuccessfulResult, unsuccessfulResult);
        assertEquals(expectedSuccessfulResult, successfulResult);
    }

    @Test
    void tryNumberAndGetException() {
        //given
        gameService.addDigit(0);
        gameService.addDigit(1);
        gameService.addDigit(2);

        //when
        Executable throwing = () -> gameService.tryNumber();

        //then
        assertThrows(NotEnoughDigitsException.class, throwing);
    }
}