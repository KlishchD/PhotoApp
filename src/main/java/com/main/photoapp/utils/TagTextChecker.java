package com.main.photoapp.utils;

import java.util.regex.Pattern;

public class TagTextChecker {
    public static final String TAG_TEXT_PATTERN = "([a-z]|[A-Z]|~|`|!|@|#|$|%|^|&|\\*|(|)|_|-|\\+|=|\\{|[|}|]|||\\|:|;|\"|'|<|,|>|.|\\?|/)*";
    public static final int TAG_TEXT_MIN_SIZE = 1;
    public static final int TAG_TEXT_MAX_SIZE = 25;

    public static boolean isTagTextCorrect(String name) {
        if (name.length() < TAG_TEXT_MIN_SIZE) return false;
        if (name.length() > TAG_TEXT_MAX_SIZE) return false;
        return Pattern.compile(TAG_TEXT_PATTERN).matcher(name).matches();
    }
}
