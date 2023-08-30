package com.woowa.woowakit.global.error;

import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends WooWaException {

    public NotFoundMemberException() {
        super("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST);
    }
}
