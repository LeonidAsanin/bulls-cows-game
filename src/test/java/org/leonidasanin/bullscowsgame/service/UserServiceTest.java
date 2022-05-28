package org.leonidasanin.bullscowsgame.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.mockito.Mock;
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

        //when

        //then

    }

    @Test
    void registerNewUser() {
        //given

        //when

        //then

    }

    @Test
    void getAverageAttemptNumberToWinByUserId() {
        //given

        //when

        //then

    }
}