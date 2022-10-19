package com.main.photoapp.utils;

public class PasswordChecker {
    public static final int PASSWORD_MINIMAL_SIZE = 5;
    public static final int PASSWORD_MAXIMAL_SIZE = 25;

    public static final String PASSWORD_PATTERN = "([a-z]|[A-Z]|~|`|!|@|#|$|%|^|&|\\*|(|)|_|-|\\+|=|\\{|[|}|]|||\\|:|;|\"|'|<|,|>|.|\\?|/)*";

    public static boolean isPasswordCorrect(String password) {
        if (password.length() < PASSWORD_MINIMAL_SIZE) return false;
        if (password.length() > PASSWORD_MAXIMAL_SIZE) return false;
        return password.matches(PASSWORD_PATTERN);
    }
}
