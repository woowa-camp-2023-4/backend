package com.woowa.woowakit.shop.order.exception;

import org.springframework.http.HttpStatus;

import com.woowa.woowakit.global.error.WooWaException;

public class QuantityNotEnoughException extends WooWaException {

	public QuantityNotEnoughException() {
		super("상품의 재고가 부족합니다", HttpStatus.BAD_REQUEST);
	}
}
