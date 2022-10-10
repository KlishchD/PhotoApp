package com.main.photoapp.controllers;

import com.main.photoapp.exceptions.EmailIsAlreadyConnectedToUserException;
import com.main.photoapp.exceptions.NicknameIsAlreadyTakenException;
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
    public int createUser(@RequestParam String nickname, @RequestParam String email, @RequestParam String password) throws NicknameIsAlreadyTakenException, EmailIsAlreadyConnectedToUserException {
        return service.createUser(nickname, email, password);
    }

    @GetMapping("/user/find/by/nickname")
    @ResponseBody
    public User getUser(@RequestParam String nickname) {
        return service.getUserByNickname(nickname);
    }


    @GetMapping("/user/find/by/id")
    @ResponseBody
    public User getUser(@RequestParam int id) {
        return service.getUserById(id);
    }

    @GetMapping("/user/remove")
    @ResponseBody
    public void removeUser(@RequestParam int id) {
        service.removeUser(id);
    }

    @PostMapping("/user/update/email")
    @ResponseBody
    public void updateUserEmail(@RequestParam int id, @RequestParam String email) {
        service.updateUserEmail(id, email);
    }

    @PostMapping("/user/update/password")
    @ResponseBody
    public void updateUserPassword(@RequestParam int id, @RequestParam String password) {
        service.updateUserPassword(id, password);
    }

}
