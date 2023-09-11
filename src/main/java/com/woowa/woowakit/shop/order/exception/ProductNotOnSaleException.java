package com.woowa.woowakit.shop.order.exception;

import org.springframework.http.HttpStatus;

public class ProductNotOnSaleException extends OrderException {

	public ProductNotOnSaleException() {
		super("판매 중이 아닌 상품입니다.", HttpStatus.BAD_REQUEST);
	}
}
