package org.leonidasanin.bullscowsgame.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.service.GameService;
import org.leonidasanin.bullscowsgame.service.UserService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameControllerTest {
    @MockBean
    GameService gameServiceMock;

    @MockBean
    UserService userServiceMock;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    User user;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        user = new User();
        user.setId(0L);
        user.setUsername("username");
        user.setPassword("password");
    }

    @Test
    void getPage() throws Exception {
        //given
       String userNumber = "1234";
        double averageAttemptNumberToWin = 10.2;
        String result = "result";

        //when
        Mockito.when(userServiceMock.getAverageAttemptNumberToWinByUserId(user.getId()))
                .thenReturn(averageAttemptNumberToWin);
        Mockito.when(gameServiceMock.getNumber())
                .thenReturn(userNumber);
        Mockito.when(gameServiceMock.getResult())
                .thenReturn(result);
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/game")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors
                                .authentication(new AuthenticationForTestOfControllers(user)))
                )
        //then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.view().name("game"))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("userNumber", userNumber))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("averageAttemptNumberToWin", averageAttemptNumberToWin))
                .andExpect(MockMvcResultMatchers.model()
                        .attribute("result", result))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    void enterDigit() throws Exception {
        //given
        int digit = 0;
        String url = "/game/enter/" + digit;

        //when
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors
                                .authentication(new AuthenticationForTestOfControllers(user)))
                )
        //then
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/game"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
        Mockito.verify(gameServiceMock)
                .addDigit(digit);
    }

    @Test
    void deleteDigit() throws Exception {
        //given
        String url = "/game/delete";

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors
                                        .authentication(new AuthenticationForTestOfControllers(user)))
                )
        //then
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/game"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
        Mockito.verify(gameServiceMock)
                .deleteDigit();
    }

    @Test
    void tryNumber() throws Exception {
        //given
        String url = "/game/try";

        //when
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post(url)
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors
                                        .authentication(new AuthenticationForTestOfControllers(user)))
                )
                //then
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/game"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
        Mockito.verify(gameServiceMock)
                .tryNumber();
    }
}