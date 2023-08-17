package com.woowa.woowakit.global.error;

import org.springframework.http.HttpStatus;

public class TokenInvalidException extends WooWaException {

    public TokenInvalidException() {
        super("유효한 토큰이 아닙니다.", HttpStatus.UNAUTHORIZED);
    }
}
