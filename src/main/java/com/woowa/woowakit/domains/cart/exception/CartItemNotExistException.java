package com.woowa.woowakit.domains.cart.exception;

import org.springframework.http.HttpStatus;

public class CartItemNotExistException extends CartException {

	public CartItemNotExistException() {
		super("회원님의 장바구니에 상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
	}
}
