package com.woowa.woowakit.domains.product.exception;

import org.springframework.http.HttpStatus;

public class StockQuantityNegativeException extends ProductException {

	public StockQuantityNegativeException() {
		super("재고 수량은 0보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST);
	}
}
