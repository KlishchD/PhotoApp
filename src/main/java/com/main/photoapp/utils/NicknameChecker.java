package com.main.photoapp.utils;

public class NicknameChecker {

    public static final int NICKNAME_MINIMAL_SIZE = 5;
    public static final int NICKNAME_MAXIMAL_SIZE = 25;

    public static final String NICKNAME_PATTERN = "([a-z]|[A-Z]|[0-9])*";

    public static boolean isNicknameCorrect(String nickname) {
        if (nickname.length() < NICKNAME_MINIMAL_SIZE) return false;
        if (nickname.length() > NICKNAME_MAXIMAL_SIZE) return false;
        return nickname.matches(NICKNAME_PATTERN);
    }

}
