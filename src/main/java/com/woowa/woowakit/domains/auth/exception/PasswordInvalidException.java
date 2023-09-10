package com.woowa.woowakit.domains.auth.exception;

import org.springframework.http.HttpStatus;

public class PasswordInvalidException extends MemberException {

    public PasswordInvalidException() {
        super("비밀번호는 하나 이상의 소문자를 포함한 7글자 이상 18글자 이하여야 합니다.", HttpStatus.BAD_REQUEST);
    }
}
