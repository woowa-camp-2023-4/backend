package com.woowa.woowakit.domains.order.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends OrderException {

    public OrderNotFoundException() {
        super("존재하지 않는 주문입니다.", HttpStatus.BAD_REQUEST);
    }

}
