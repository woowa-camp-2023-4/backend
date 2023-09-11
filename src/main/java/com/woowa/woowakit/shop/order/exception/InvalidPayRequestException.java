package com.woowa.woowakit.shop.order.exception;

import org.springframework.http.HttpStatus;

public class InvalidPayRequestException extends OrderException {

	public InvalidPayRequestException(final Throwable cause) {
		super(cause.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
