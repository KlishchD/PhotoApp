package com.main.photoapp.services;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.main.photoapp.utils.EmailChecker.isEmailCorrect;
import static com.main.photoapp.utils.NicknameChecker.isNicknameCorrect;
import static com.main.photoapp.utils.PasswordChecker.isPasswordCorrect;

@Component
public class UsersService {
    @Autowired
    private UsersRepository users;

    public int createUser(String nickname, String email, String password) throws NicknameIsAlreadyTakenException, EmailIsAlreadyTakenException, IncorrectEmailFormat, IncorrectPasswordFormat, IncorrectNicknameFormat {
        if (!isNicknameCorrect(nickname)) throw new IncorrectNicknameFormat(nickname);
        if (!isEmailCorrect(email)) throw new IncorrectEmailFormat(email);
        if (!isPasswordCorrect(password)) throw new IncorrectPasswordFormat(password);
        if (users.existsByNickname(nickname)) throw new NicknameIsAlreadyTakenException(nickname);
        if (users.existsByEmail(email)) throw new EmailIsAlreadyTakenException(email);
        return users.save(new User(nickname, email, password)).getId();
    }

    public User getUserByNickname(String nickname) throws UserNotFoundException, IncorrectNicknameFormat {
        if (!isNicknameCorrect(nickname)) throw new IncorrectNicknameFormat(nickname);
        if (!users.existsByNickname(nickname)) throw new UserNotFoundException(nickname);
        return users.findByNickname(nickname).get();
    }

    public User getUserById(int id) throws UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        return users.findById(id).get();
    }

    public void removeUser(int id) throws UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        users.deleteById(id);
    }

    public void updateUserEmail(int id, String email) throws IncorrectEmailFormat, UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        if (!isEmailCorrect(email)) throw new IncorrectEmailFormat(email);
        User user = users.findById(id).orElseThrow();
        user.setEmail(email);
        users.save(user);
    }

    public void updateUserPassword(int id, String password) throws IncorrectPasswordFormat, UserNotFoundException {
        if (!users.existsById(id)) throw new UserNotFoundException(id);
        if (!isPasswordCorrect(password)) throw new IncorrectPasswordFormat(password);
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
