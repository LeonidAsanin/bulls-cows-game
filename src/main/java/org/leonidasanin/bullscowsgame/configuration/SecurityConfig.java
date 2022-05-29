package org.leonidasanin.bullscowsgame.configuration;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

/**
 * Security configuration that adds BCryptPasswordEncoder and UserDetailsService as beans and
 * sets access limitations for unauthenticated users along with login/logout settings.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            return optionalUser
                    .orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found"));
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeRequests()
                    .antMatchers("/login", "/register").permitAll()
                    .antMatchers("/style.css").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/game")
                .and()
                .logout()
                .and()
                .build();
    }
}
