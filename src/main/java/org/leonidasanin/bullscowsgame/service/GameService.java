package org.leonidasanin.bullscowsgame.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@SessionScope
public class GameService {
    private final Deque<Integer> NUMBER = new LinkedList<>();

    public void addDigit(int digit) {
        if (NUMBER.size() < 4 && !NUMBER.contains(digit)) NUMBER.addLast(digit);
    }

    public void deleteDigit() {
        NUMBER.pollLast();
    }

    public void tryNumber() {
        System.out.println("try: " + NUMBER);
    }

    public Optional<Integer> getNumber() {
        String stringRepresentation = NUMBER.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
        try {
            Integer integer = Integer.valueOf(stringRepresentation);
            return Optional.of(integer);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
