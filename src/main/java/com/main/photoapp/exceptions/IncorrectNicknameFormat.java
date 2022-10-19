package com.main.photoapp.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Nickname is in incorrect format")
public class IncorrectNicknameFormat extends Exception {
    public IncorrectNicknameFormat(String nickname) {
        super("Nickname : " + nickname + " is incorrect format");
    }
}
