package com.htm.e20nomics.global.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("유저를 찾을수 없습니다.");
    }
}
