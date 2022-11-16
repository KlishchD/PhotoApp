package com.main.photoapp.controllers.internal;

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
    public int createUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) throws UsernameIsAlreadyTakenException, EmailIsAlreadyTakenException, IncorrectEmailFormat, IncorrectUsernameFormat, IncorrectPasswordFormat {
        return service.createUser(username, email, password);
    }

    @GetMapping("/user/find/by/username")
    @ResponseBody
    public User getUser(@RequestParam String username) throws UserNotFoundException, IncorrectUsernameFormat {
        return service.getUserByUsername(username);
    }


    @GetMapping("/user/find/by/id")
    @ResponseBody
    public User getUser(@RequestParam int id) throws UserNotFoundException {
        return service.getUserById(id);
    }

    @PostMapping("/user/remove")
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
