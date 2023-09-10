package com.woowa.woowakit.domains.order.exception;

import org.springframework.http.HttpStatus;

public class NotMyOrderException extends OrderException {

    public NotMyOrderException() {
        super("본인의 주문이 아닙니다.", HttpStatus.BAD_REQUEST);
    }
}
