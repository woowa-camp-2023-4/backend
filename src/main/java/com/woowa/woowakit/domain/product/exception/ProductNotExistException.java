package com.woowa.woowakit.domain.product.exception;

import org.springframework.http.HttpStatus;

public class ProductNotExistException extends ProductException {
	public ProductNotExistException() {
		super("존재하지 않은 상품 정보입니다.", HttpStatus.NOT_FOUND);
	}
}
