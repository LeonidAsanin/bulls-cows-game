package org.leonidasanin.bullscowsgame.model;

import java.util.List;

/**
 * Record representing result of the given game attempt.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
public class GameResult {
    private final boolean success;
    private final String secretNumber;
    private final List<String> attemptBullsAndCowsList;

    public GameResult(boolean success, String secretNumber, List<String> attemptBullsAndCowsList) {
        this.success = success;
        this.secretNumber = secretNumber;
        this.attemptBullsAndCowsList = attemptBullsAndCowsList;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSecretNumber() {
        return secretNumber;
    }

    public List<String> getAttemptBullsAndCowsList() {
        return attemptBullsAndCowsList;
    }
}
