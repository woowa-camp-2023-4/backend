package com.woowa.woowakit.domains.auth.exception;

import org.springframework.http.HttpStatus;

public class EmailInvalidException extends MemberException {

    public EmailInvalidException() {
        super("이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
