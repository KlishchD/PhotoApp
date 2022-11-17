package com.main.photoapp.Utils;

import com.main.photoapp.utils.TagTextChecker;

import java.util.Random;

import static com.main.photoapp.utils.DeskDescriptionChecker.DESK_DESCRIPTION_MAX_SIZE;
import static com.main.photoapp.utils.DeskDescriptionChecker.DESK_DESCRIPTION_MIN_SIZE;
import static com.main.photoapp.utils.DeskNameChecker.DESK_NAME_MAX_SIZE;
import static com.main.photoapp.utils.DeskNameChecker.DESK_NAME_MIN_SIZE;
import static com.main.photoapp.utils.UsernameChecker.USERNAME_MAXIMAL_SIZE;
import static com.main.photoapp.utils.UsernameChecker.USERNAME_MINIMAL_SIZE;
import static com.main.photoapp.utils.PasswordChecker.PASSWORD_MAXIMAL_SIZE;
import static com.main.photoapp.utils.PasswordChecker.PASSWORD_MINIMAL_SIZE;
import static com.main.photoapp.utils.TagTextChecker.TAG_TEXT_MAX_SIZE;
import static com.main.photoapp.utils.TagTextChecker.TAG_TEXT_MIN_SIZE;

public class RandomTextGenerator {
    private static final String TEXT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DEFAULT_DOMAIN = "domain.com";

    public static synchronized String getRandomText(int size, Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            int pos = random.nextInt(0, TEXT_CHARACTERS.length());
            builder.append(TEXT_CHARACTERS.charAt(pos));
        }
        return builder.toString();
    }

    public static synchronized String getRandomEmail(Random random) {
        int size = random.nextInt(0, 65);
        return getRandomText(size, random) + "@" + DEFAULT_DOMAIN;
    }

    public static synchronized String getRandomPassword(Random random) {
        int size = random.nextInt(PASSWORD_MINIMAL_SIZE, PASSWORD_MAXIMAL_SIZE + 1);
        return getRandomText(size, random);
    }

    public static synchronized String getRandomUsername(Random random) {
        int size = random.nextInt(USERNAME_MINIMAL_SIZE, USERNAME_MAXIMAL_SIZE + 1);
        return getRandomText(size, random);
    }

    public static synchronized String getRandomName(Random random) {
        int size = random.nextInt(DESK_NAME_MIN_SIZE, DESK_NAME_MAX_SIZE + 1);
        return getRandomText(size, random);
    }

    public static synchronized String getRandomDescription(Random random) {
        int size = random.nextInt(DESK_DESCRIPTION_MIN_SIZE, DESK_DESCRIPTION_MAX_SIZE);
        return getRandomText(size, random);
    }

    public static synchronized String getRandomTagText(Random random) {
        int size = random.nextInt(TAG_TEXT_MIN_SIZE, TAG_TEXT_MAX_SIZE);
        return getRandomText(size, random);
    }
}
