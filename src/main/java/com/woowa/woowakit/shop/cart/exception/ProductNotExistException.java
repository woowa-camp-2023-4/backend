package com.woowa.woowakit.shop.cart.exception;

import org.springframework.http.HttpStatus;

public class ProductNotExistException extends CartException {

	public ProductNotExistException() {
		super("존재하지 않은 상품 정보입니다.", HttpStatus.NOT_FOUND);
	}
}
