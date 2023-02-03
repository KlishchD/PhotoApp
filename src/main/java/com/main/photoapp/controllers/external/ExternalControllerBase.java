package com.main.photoapp.controllers.external;


import com.main.photoapp.models.User;
import com.main.photoapp.services.UsersService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class ExternalControllerBase {
    protected final UsersService usersService;

        public ExternalControllerBase(UsersService usersService) {
        this.usersService = usersService;
    }

    @SneakyThrows
    public User getCurrentUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersService.getUserByUsername(name);
    }
}
