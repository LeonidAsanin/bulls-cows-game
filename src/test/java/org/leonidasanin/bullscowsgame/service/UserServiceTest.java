package org.leonidasanin.bullscowsgame.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    UserService userService;

    @Mock
    UserRepository userRepositoryMock;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepositoryMock);
    }

    @Test
    void existsByUsername() {
        //given
        String username = "username";
        boolean expectedResult = true;

        //when
        Mockito.when(userRepositoryMock.existsByUsernameIgnoreCase(username))
                .thenReturn(expectedResult);
        boolean result = userService.existsByUsername(username);

        //then
        assertEquals(expectedResult, result);

    }

    @Test
    void registerNewUser() {
        //given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");

        //when
        userService.registerNewUser(user);

        //then
        Mockito.verify(userRepositoryMock)
                .save(user);

    }

    @Test
    void getAverageAttemptNumberToWinByUserId() {
        //given
        long userId = 0L;
        double expectedResult = 5.5;

        //when
        Mockito.when(userRepositoryMock.getAverageAttemptNumberToWinByUserId(userId))
                .thenReturn(expectedResult);
        double averageAttemptNumberToWin = userService.getAverageAttemptNumberToWinByUserId(userId);

        //then
        assertEquals(expectedResult, averageAttemptNumberToWin);
    }
}