package com.woowa.woowakit.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class InvalidProductInCartItemException extends CartException {

    public InvalidProductInCartItemException() {
        super("상품을 구매할 수 없는 상태입니다.", HttpStatus.BAD_REQUEST);
    }
}
