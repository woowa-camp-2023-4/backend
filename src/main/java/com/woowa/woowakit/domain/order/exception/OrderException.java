package com.woowa.woowakit.domain.order.exception;

import com.woowa.woowakit.global.error.WooWaException;
import org.springframework.http.HttpStatus;

public class OrderException extends WooWaException {

    public OrderException(HttpStatus httpStatus) {
        super(httpStatus);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause, httpStatus);
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
