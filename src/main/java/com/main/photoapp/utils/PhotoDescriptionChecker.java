package com.main.photoapp.utils;

import java.util.regex.Pattern;

public class PhotoDescriptionChecker {
    public static final String PHOTO_DESCRIPTION_PATTERN = "([a-z]|[A-Z]|~|`|!|@|#|$|%|^|&|\\*|(|)|_|-|\\+|=|\\{|[|}|]|||\\|:|;|\"|'|<|,|>|.|\\?|/)*";
    public static final int PHOTO_DESCRIPTION_MIN_SIZE = 5;
    public static final int PHOTO_DESCRIPTION_MAX_SIZE = 255;

    public static boolean isPhotoDescriptionCorrect(String name) {
        if (name.length() < PHOTO_DESCRIPTION_MIN_SIZE) return false;
        if (name.length() > PHOTO_DESCRIPTION_MAX_SIZE) return false;
        return Pattern.compile(PHOTO_DESCRIPTION_PATTERN).matcher(name).matches();
    }
}
