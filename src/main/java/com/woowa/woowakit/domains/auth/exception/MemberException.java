package com.woowa.woowakit.domains.auth.exception;

import com.woowa.woowakit.global.error.WooWaException;
import org.springframework.http.HttpStatus;

public class MemberException extends WooWaException {

    public MemberException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
