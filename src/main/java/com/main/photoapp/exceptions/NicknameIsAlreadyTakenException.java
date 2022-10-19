package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Nickname is already taken")
public class NicknameIsAlreadyTakenException extends Exception {

    public NicknameIsAlreadyTakenException(String nickname) {
        super("User with nickname: " + nickname + " already exists");
    }
}
