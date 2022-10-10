package com.main.photoapp.services;

import com.main.photoapp.exceptions.EmailIsAlreadyConnectedToUserException;
import com.main.photoapp.exceptions.NicknameIsAlreadyTakenException;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class UsersService {
    @Autowired
    private UsersRepository users;

    public int createUser(String nickname, String email, String password) throws NicknameIsAlreadyTakenException, EmailIsAlreadyConnectedToUserException {
        if (users.existsByNickname(nickname)) throw new NicknameIsAlreadyTakenException(nickname);
        if (users.existsByEmail(email)) throw new EmailIsAlreadyConnectedToUserException(email);
        return users.save(new User(nickname, email, password)).getId();
    }

    public User getUserByNickname(String nickname) {
        return users.findByNickname(nickname).orElse(null);
    }

    public User getUserById(int id) {
        return users.findById(id).orElse(null);
    }

    public void removeUser(int id) {
        users.deleteById(id);
    }

    public void updateUserEmail(int id, String email) {
        User user = users.findById(id).orElseThrow();
        user.setEmail(email);
        users.save(user);
    }

    public void updateUserPassword(int id, String password) {
        User user = users.findById(id).orElseThrow();
        user.setPassword(password);
        users.save(user);
    }

    public boolean userExists(int id) {
        return users.existsById(id);
    }

    public boolean userNotExists(int id) {
        return !users.existsById(id);
    }

}
