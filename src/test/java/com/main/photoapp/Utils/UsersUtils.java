package com.main.photoapp.Utils;

import com.main.photoapp.services.UsersService;
import com.main.photoapp.utils.NicknameChecker;
import com.main.photoapp.utils.PasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UsersUtils {
    private static final String EMAIL_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DEFAULT_DOMAIN = "domain.com";

    @Autowired
    private UsersService service;

    public String getRandomText(int size, Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            int pos = random.nextInt(0, EMAIL_CHARACTERS.length());
            builder.append(EMAIL_CHARACTERS.charAt(pos));
        }
        return builder.toString();
    }

    public synchronized String getRandomEmail(Random random) {
        int size = random.nextInt(0, 65);
        return getRandomText(size, random) + "@" + DEFAULT_DOMAIN;
    }

    public synchronized String getRandomPassword(Random random) {
        int size = random.nextInt(PasswordChecker.PASSWORD_MINIMAL_SIZE, PasswordChecker.PASSWORD_MAXIMAL_SIZE + 1);
        return getRandomText(size, random);
    }

    public synchronized String getRandomNickname(Random random) {
        int size = random.nextInt(NicknameChecker.NICKNAME_MINIMAL_SIZE, NicknameChecker.NICKNAME_MAXIMAL_SIZE + 1);
        return getRandomText(size, random);
    }

    public String createUser(String nickname, String email, String password) throws Exception {
        return String.valueOf(service.createUser(nickname, email, password));
    }

    public String createUser(Random random) throws Exception {
        return String.valueOf(service.createUser(getRandomNickname(random), getRandomEmail(random), getRandomPassword(random)));
    }
}
