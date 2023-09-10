package com.woowa.woowakit.domains.product.exception;

import org.springframework.http.HttpStatus;

public class StockBatchFailException extends ProductException {

	public StockBatchFailException(Exception e) {
		super("배치 처리 실패", e, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
