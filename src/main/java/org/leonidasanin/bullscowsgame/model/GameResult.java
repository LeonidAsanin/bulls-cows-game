package org.leonidasanin.bullscowsgame.model;

import java.util.List;

public class GameResult {
    private final boolean success;
    private final int secretNumber;
    private final List<String> attemptBullsAndCowsList;

    public GameResult(boolean success, int secretNumber, List<String> attemptBullsAndCowsList) {
        this.success = success;
        this.secretNumber = secretNumber;
        this.attemptBullsAndCowsList = attemptBullsAndCowsList;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public List<String> getAttemptBullsAndCowsList() {
        return attemptBullsAndCowsList;
    }
}
