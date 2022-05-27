package org.leonidasanin.bullscowsgame.service;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public void registerNewUser(User user) {
        userRepository.save(user);
    }

    public double getAverageAttemptNumberToWinByUserId(long userId) {
        return userRepository.getAverageAttemptNumberToWinByUserId(userId);
    }
}
