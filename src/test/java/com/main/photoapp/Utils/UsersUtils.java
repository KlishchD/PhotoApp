package com.main.photoapp.Utils;

import com.main.photoapp.exceptions.IncorrectUsernameFormat;
import com.main.photoapp.exceptions.UserNotFoundException;
import com.main.photoapp.models.User;
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

    @Autowired
    private UsersRepository repository;


    public String createUser(String username, String email, String password) throws Exception {
        return String.valueOf(service.createUser(username, email, password));
    }

    public String createUser(Random random) throws Exception {
        return String.valueOf(service.createUser(getRandomUsername(random), getRandomEmail(random), getRandomPassword(random)));
    }

    public void removeUser(String username) throws UserNotFoundException, IncorrectUsernameFormat {
        User user = service.getUserByUsername(username);
        service.removeUser(user.getId());
    }

    public void clearRepository() {
        repository.deleteAll();
    }
}
