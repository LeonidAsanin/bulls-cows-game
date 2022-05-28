package org.leonidasanin.bullscowsgame.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.leonidasanin.bullscowsgame.controller.AuthenticationForTestOfControllers;
import org.leonidasanin.bullscowsgame.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
class SecurityConfigTest {
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
    void logoutTest() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/logout")
                                .with(SecurityMockMvcRequestPostProcessors.csrf())
                                .with(SecurityMockMvcRequestPostProcessors
                                        .authentication(new AuthenticationForTestOfControllers()))
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?logout"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Nested
    class unauthenticatedUserTests {
        @Test
        void getCallsTest() {
            assertAll(
                    () -> {
                        String[] urlsForGetRequests = {"/", "/game"};
                        for (String url : urlsForGetRequests) {
                            mockMvc.perform(
                                            MockMvcRequestBuilders
                                                    .get(url)
                                    )
                                    .andExpect(MockMvcResultMatchers.status().is(302))
                                    .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/login"))
                                    .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
                        }
                    },
                    () -> {
                        String[] urlsForGetRequests = {"/login", "/register"};
                        for (String url : urlsForGetRequests) {
                            mockMvc.perform(
                                            MockMvcRequestBuilders
                                                    .get(url)
                                    )
                                    .andExpect(MockMvcResultMatchers.status().isOk())
                                    .andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
                                    .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
                        }
                    }
            );
        }

        @Test
        void postGameTest() {
            assertAll(
                    () -> mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/game/try")
                            )
                            .andExpect(MockMvcResultMatchers.status().is(403))
                            .andExpect(SecurityMockMvcResultMatchers.unauthenticated()),
                    () -> mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/game/delete")
                            )
                            .andExpect(MockMvcResultMatchers.status().is(403))
                            .andExpect(SecurityMockMvcResultMatchers.unauthenticated()),
                    () -> mockMvc.perform(
                                    MockMvcRequestBuilders
                                            .post("/game/enter/1")
                            )
                            .andExpect(MockMvcResultMatchers.status().is(403))
                            .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
            );
        }

        @Test
        void postRegisterTest() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/register")
                                    .param("username", "username")
                                    .param("password", "password")
                                    .param("passwordConfirmation", "password")
                    )
                    .andExpect(MockMvcResultMatchers.status().is(403))
                    .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
        }
    }
}