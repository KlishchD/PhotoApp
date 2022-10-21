package com.main.photoapp.Utils;

import com.main.photoapp.repositories.UsersRepository;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.main.photoapp.Utils.RandomTextGenerator.*;

@Component
public class UsersUtils {
    @Autowired
    private UsersService service;


    public String createUser(String nickname, String email, String password) throws Exception {
        return String.valueOf(service.createUser(nickname, email, password));
    }

    public String createUser(Random random) throws Exception {
        return String.valueOf(service.createUser(getRandomNickname(random), getRandomEmail(random), getRandomPassword(random)));
    }

}
