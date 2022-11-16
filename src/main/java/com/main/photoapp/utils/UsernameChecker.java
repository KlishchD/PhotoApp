package com.main.photoapp.utils;

public class UsernameChecker {

    public static final int NICKNAME_MINIMAL_SIZE = 5;
    public static final int NICKNAME_MAXIMAL_SIZE = 25;

    public static final String NICKNAME_PATTERN = "([a-z]|[A-Z]|[0-9])*";

    public static boolean isUsernameCorrect(String username) {
        if (username.length() < NICKNAME_MINIMAL_SIZE) return false;
        if (username.length() > NICKNAME_MAXIMAL_SIZE) return false;
        return username.matches(NICKNAME_PATTERN);
    }

}
