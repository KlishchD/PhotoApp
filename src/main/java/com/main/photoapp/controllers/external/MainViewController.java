package com.main.photoapp.controllers.external;

import com.main.photoapp.exceptions.*;
import com.main.photoapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@Controller
public class MainViewController {
    @Autowired
    private UsersService service;

    @GetMapping("/registration")
    public String registration(@RequestParam(required = false, defaultValue = "") String error, @RequestParam(required = false, defaultValue = "") String email, @RequestParam(required = false, defaultValue = "") String nickname) {
        return "registration";
    }

    @PostMapping("/perform_registration")
    public void registration(@RequestParam String nickname, @RequestParam String email, @RequestParam String password, HttpServletResponse response) throws IOException {
        try {
            service.createUser(nickname, email, password);
            response.sendRedirect("/login");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("/registration?error=" + e.getMessage() + "&email=" + email +"&nickname=" + nickname );
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false, defaultValue = "false") String error, @RequestParam(required = false, defaultValue = "false") boolean logout) {
        return "login";
    }
}
