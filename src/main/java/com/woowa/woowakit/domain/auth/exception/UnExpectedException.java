package com.woowa.woowakit.domain.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class UnExpectedException extends MemberException {

    public UnExpectedException(Throwable cause) {
        super("예기치 않은 오류가 발생했습니다.", cause, HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("예기치 않은 오류가 발생했습니다.", cause);
    }
}
