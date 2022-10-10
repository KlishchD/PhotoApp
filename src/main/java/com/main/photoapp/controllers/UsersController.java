package com.main.photoapp.controllers;

import com.main.photoapp.exceptions.EmailIsAlreadyConnectedToUserException;
import com.main.photoapp.exceptions.NicknameIsAlreadyTakenException;
import com.main.photoapp.models.User;
import com.main.photoapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsersController {
    @Autowired
    private UsersRepository users;


    @PostMapping("/user/create")
    @ResponseBody
    public int createUser(@RequestParam String nickname, @RequestParam String email, @RequestParam String password) throws NicknameIsAlreadyTakenException, EmailIsAlreadyConnectedToUserException {
        if (users.existsByNickname(nickname)) throw new NicknameIsAlreadyTakenException(nickname);
        if (users.existsByEmail(email)) throw new EmailIsAlreadyConnectedToUserException(email);
        return users.save(new User(nickname, email, password)).getId();
    }

    @GetMapping("/user/find/by/nickname")
    @ResponseBody
    public User getUser(@RequestParam String nickname) {
        return users.findByNickname(nickname).orElse(null);
    }


    @GetMapping("/user/find/by/id")
    @ResponseBody
    public User getUser(@RequestParam int id) {
        return users.findById(id).orElse(null);
    }

    @GetMapping("/user/remove")
    @ResponseBody
    public void removeUser(@RequestParam int id) {
        users.deleteById(id);
    }

    @PostMapping("/user/update/email")
    @ResponseBody
    public void updateUserEmail(@RequestParam int id, @RequestParam String email) {
        User user = users.findById(id).orElseThrow();
        user.setEmail(email);
        users.save(user);
    }

    @PostMapping("/user/update/password")
    @ResponseBody
    public void updateUserPassword(@RequestParam int id, @RequestParam String password) {
        User user = users.findById(id).orElseThrow();
        user.setPassword(password);
        users.save(user);
    }

}
