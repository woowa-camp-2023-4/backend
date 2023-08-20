package com.woowa.woowakit.domain.cart.exception;

import org.springframework.http.HttpStatus;

public class CartItemQuantityException extends CartException {

	public CartItemQuantityException() {
		super("상품 수량보다 많은 수량을 장바구니에 담을 수 없습니다.", HttpStatus.BAD_REQUEST);
	}
}
