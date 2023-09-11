package com.woowa.woowakit.shop.order.exception;

import org.springframework.http.HttpStatus;

public class CartItemNotFoundException extends OrderException {

	public CartItemNotFoundException() {
		super("존재하지 않은 장바구니 정보입니다.", HttpStatus.BAD_REQUEST);
	}
}
