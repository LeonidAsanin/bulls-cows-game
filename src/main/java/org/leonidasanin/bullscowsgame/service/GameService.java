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

/**
 * Service for managing user's game.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
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
    private String result;

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

    private GameResult calculateResult() throws NotEnoughDigitsException {
        if (CURRENT_NUMBER.size() < 4) throw new NotEnoughDigitsException();

        for (int i = 0; i < 4; i++) {
            if (SECRET_NUMBER.contains(CURRENT_NUMBER.get(i))) {
                if (SECRET_NUMBER.get(i).equals(CURRENT_NUMBER.get(i))) {
                    bulls++;
                } else {
                    cows++;
                }
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

    public void addDigit(int digit) {
        if (CURRENT_NUMBER.size() < 4 && !CURRENT_NUMBER.contains(digit)) CURRENT_NUMBER.addLast(digit);
    }

    public void deleteDigit() {
        CURRENT_NUMBER.pollLast();
    }

    public String getNumber() {
        return CURRENT_NUMBER.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public String getSecretNumber() {
        return SECRET_NUMBER.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public void setSecretNumber(LinkedList<Integer> secretNumber) throws NumberFormatException {
        Set<Integer> digitSet = new HashSet<>(secretNumber);
        if (digitSet.size() != 4) throw new NumberFormatException();
        for (int digit : digitSet) {
            if (digit < 0 || digit > 9) throw new NumberFormatException();
        }

        SECRET_NUMBER.clear();

        for (int digit : secretNumber) {
            SECRET_NUMBER.addLast(digit);
        }
    }

    public void tryNumber() throws NotEnoughDigitsException {
        StringBuilder resultBuilder = new StringBuilder();

        GameResult gameResult = calculateResult();
        if (gameResult.isSuccess()) {
            resultBuilder.append("You won! Secret number is ")
                    .append(gameResult.getSecretNumber())
                    .append(". Attempts: ")
                    .append(gameResult.getAttemptBullsAndCowsList().size())
                    .append("<br/> Try to guess new one!");
        } else {
            resultBuilder.append("Previous tries: <br/>");

            int tryCounter = 1;
            for (String attempt : gameResult.getAttemptBullsAndCowsList()) {
                resultBuilder.append("[")
                        .append(tryCounter++)
                        .append("] ")
                        .append(attempt)
                        .append("<br/>");
            }
        }

        result =  resultBuilder.toString();
    }

    public String getResult() {
        return result;
    }
}
