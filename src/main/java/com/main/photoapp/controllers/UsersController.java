package com.main.photoapp.controllers;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.models.User;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UsersController {
    @Autowired
    private UsersService service;


    @PostMapping("/user/create")
    @ResponseBody
    public int createUser(@RequestParam String nickname, @RequestParam String email, @RequestParam String password) throws NicknameIsAlreadyTakenException, EmailIsAlreadyTakenException, IncorrectEmailFormat, IncorrectNicknameFormat, IncorrectPasswordFormat {
        return service.createUser(nickname, email, password);
    }

    @GetMapping("/user/find/by/nickname")
    @ResponseBody
    public User getUser(@RequestParam String nickname) throws UserNotFoundException, IncorrectNicknameFormat {
        return service.getUserByNickname(nickname);
    }


    @GetMapping("/user/find/by/id")
    @ResponseBody
    public User getUser(@RequestParam int id) throws UserNotFoundException {
        return service.getUserById(id);
    }

    @GetMapping("/user/remove")
    @ResponseBody
    public void removeUser(@RequestParam int id) throws UserNotFoundException {
        service.removeUser(id);
    }

    @PostMapping("/user/update/email")
    @ResponseBody
    public void updateUserEmail(@RequestParam int id, @RequestParam String email) throws UserNotFoundException, IncorrectEmailFormat {
        service.updateUserEmail(id, email);
    }

    @PostMapping("/user/update/password")
    @ResponseBody
    public void updateUserPassword(@RequestParam int id, @RequestParam String password) throws UserNotFoundException, IncorrectPasswordFormat {
        service.updateUserPassword(id, password);
    }

}
