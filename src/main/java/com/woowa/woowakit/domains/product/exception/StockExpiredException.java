package com.woowa.woowakit.domains.product.exception;

import org.springframework.http.HttpStatus;

public class StockExpiredException extends ProductException {

	public StockExpiredException() {
		super("소비 기한이 지난 재고 항목입니다.", HttpStatus.BAD_REQUEST);
	}
}
