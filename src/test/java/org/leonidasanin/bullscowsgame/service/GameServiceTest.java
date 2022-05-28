package org.leonidasanin.bullscowsgame.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    GameService gameService;

    @Mock
    UserRepository userRepositoryMock;

    @BeforeEach
    void setUp() {
        gameService = new GameService(userRepositoryMock);
    }

    @Test
    void addDigit() {
    }

    @Test
    void deleteDigit() {
    }

    @Test
    void tryNumber() {
    }

    @Test
    void getNumber() {
    }
}