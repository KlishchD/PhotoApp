package com.main.photoapp.exceptions;

public class NicknameIsAlreadyTakenException extends Exception {

    public NicknameIsAlreadyTakenException(String nickname) {
        super("User with nickname: " + nickname + " already exists");
    }
}
