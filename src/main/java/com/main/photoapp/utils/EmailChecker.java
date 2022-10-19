package com.main.photoapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailChecker {

    public static final String EMAIL_PATTERN = "(^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$)";

    public static boolean isEmailCorrect(String string) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

}
