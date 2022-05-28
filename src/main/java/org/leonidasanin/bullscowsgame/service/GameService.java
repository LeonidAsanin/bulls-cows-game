package org.leonidasanin.bullscowsgame.service;

import org.leonidasanin.bullscowsgame.entity.User;
import org.leonidasanin.bullscowsgame.exception.NotEnoughDigitsException;
import org.leonidasanin.bullscowsgame.model.GameResult;
import org.leonidasanin.bullscowsgame.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
import java.util.stream.Collectors;

@Service
@SessionScope
public class GameService {
    private final LinkedList<Integer> CURRENT_NUMBER;
    private final LinkedList<Integer> SECRET_NUMBER;
    private final List<String> attemptAndBullsCowsList;
    private final UserRepository userRepository;
    private final long userId;

    private int bulls;
    private int cows;

    public GameService(UserRepository userRepository) {
        this.userRepository = userRepository;

        CURRENT_NUMBER = new LinkedList<>();
        attemptAndBullsCowsList = new LinkedList<>();
        SECRET_NUMBER = new LinkedList<>();

        userId = ((User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                .getId();

        generateNewSecretNumber();
    }

    private void generateNewSecretNumber() {
        SECRET_NUMBER.clear();

        Random random = new Random();
        while (SECRET_NUMBER.size() != 4) {
            int digit = random.nextInt(10);
            if (!SECRET_NUMBER.contains(digit)) {
                SECRET_NUMBER.add(digit);
            }
        }
        System.out.println("SECRET_NUMBER" + SECRET_NUMBER);
    }

    private int getSecretNumber() {
        return Integer.parseInt(
                SECRET_NUMBER.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining())
        );
    }

    public void addDigit(int digit) {
        if (CURRENT_NUMBER.size() < 4 && !CURRENT_NUMBER.contains(digit)) CURRENT_NUMBER.addLast(digit);
    }

    public void deleteDigit() {
        CURRENT_NUMBER.pollLast();
    }

    public GameResult tryNumber() throws NotEnoughDigitsException {
        if (CURRENT_NUMBER.size() < 4) throw new NotEnoughDigitsException();

        for (int i = 0; i < 4; i++) {
            if (SECRET_NUMBER.contains(CURRENT_NUMBER.get(i))) {
                if (SECRET_NUMBER.get(i).equals(CURRENT_NUMBER.get(i))) {
                    bulls++;
                }
                cows++;
            }
        }

        attemptAndBullsCowsList.add(
                CURRENT_NUMBER.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining())
                        .concat(" [" + bulls + "(bulls) and " + cows + "(cows)]")
        );

        GameResult gameResult = new GameResult(
                bulls == 4,
                getSecretNumber(),
                new ArrayList<>(attemptAndBullsCowsList)
        );

        if (bulls == 4) {
            generateNewSecretNumber();
            int gameCount = userRepository.getGameCountByUserId(userId);
            double averageAttemptNumberToWin = userRepository.getAverageAttemptNumberToWinByUserId(userId);
            averageAttemptNumberToWin = (gameCount * averageAttemptNumberToWin + attemptAndBullsCowsList.size()) / (gameCount + 1);

            userRepository.incrementGameCountByUserId(userId);
            userRepository.setAverageAttemptNumberToWinByUserId(userId, averageAttemptNumberToWin);

            attemptAndBullsCowsList.clear();
        }

        bulls = cows = 0;
        CURRENT_NUMBER.clear();

        return gameResult;
    }

    public String getNumber() {
        return CURRENT_NUMBER.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
