package com.woowa.woowakit.global.error;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends WooWaException {

    public ForbiddenException() {
        super("관리자 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
