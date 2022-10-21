package com.main.photoapp.utils;

import java.util.regex.Pattern;

public class DeskDescriptionChecker {
    public static final String DESK_DESCRIPTION_PATTERN = "([a-z]|[A-Z]|~|`|!|@|#|$|%|^|&|\\*|(|)|_|-|\\+|=|\\{|[|}|]|||\\|:|;|\"|'|<|,|>|.|\\?|/)*";
    public static final int DESK_DESCRIPTION_MIN_SIZE = 5;
    public static final int DESK_DESCRIPTION_MAX_SIZE = 255;

    public static boolean isDeskDescriptionCorrect(String name) {
        if (name.length() < DESK_DESCRIPTION_MIN_SIZE) return false;
        if (name.length() > DESK_DESCRIPTION_MAX_SIZE) return false;
        return Pattern.compile(DESK_DESCRIPTION_PATTERN).matcher(name).matches();
    }
}
