package com.main.photoapp.utils;

import java.util.regex.Pattern;

public class DeskNameChecker {
    public static final String DESK_NAME_PATTERN = "([a-z]|[A-Z]|~|`|!|@|#|$|%|^|&|\\*|(|)|_|-|\\+|=|\\{|[|}|]|||\\|:|;|\"|'|<|,|>|.|\\?|/)*";
    public static final int DESK_NAME_MIN_SIZE = 1;
    public static final int DESK_NAME_MAX_SIZE = 25;

    public static boolean isDeskNameCorrect(String name) {
        if (name.length() < DESK_NAME_MIN_SIZE) return false;
        if (name.length() > DESK_NAME_MAX_SIZE) return false;
        return Pattern.compile(DESK_NAME_PATTERN).matcher(name).matches();
    }
}
