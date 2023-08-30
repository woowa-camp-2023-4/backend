package com.woowa.woowakit.domain.auth.exception;

import org.springframework.http.HttpStatus;

public class LoginFailException extends MemberException {

    public LoginFailException() {
        super("이메일 혹은 비밀번호를 잘못 입력하셨습니다.", HttpStatus.BAD_REQUEST);
    }
}
