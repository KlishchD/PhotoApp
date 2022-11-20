package com.main.photoapp.utils;

public class UsernameChecker {

    public static final int USERNAME_MINIMAL_SIZE = 5;
    public static final int USERNAME_MAXIMAL_SIZE = 25;

    public static final String USERNAME_PATTERN = "([a-z]|[A-Z]|[0-9])*";

    public static boolean isUsernameCorrect(String username) {
        if (username.length() < USERNAME_MINIMAL_SIZE) return false;
        if (username.length() > USERNAME_MAXIMAL_SIZE) return false;
        return username.matches(USERNAME_PATTERN);
    }

}
