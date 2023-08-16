package com.woowa.woowakit.domain.product.exception;

import org.springframework.http.HttpStatus;

public class ProductQuantityNegativeException extends ProductException {
	public ProductQuantityNegativeException() {
		super("제품 수량은 0보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST);
	}
}
