package com.woowa.woowakit.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class NotMyCartItemException extends CartException {

	public NotMyCartItemException() {
		super("본인의 장바구니 상품이 아닙니다.", HttpStatus.BAD_REQUEST);
	}

}
