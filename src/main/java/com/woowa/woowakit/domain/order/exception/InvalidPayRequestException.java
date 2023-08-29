package com.woowa.woowakit.domain.order.exception;

import org.springframework.http.HttpStatus;

public class InvalidPayRequestException extends OrderException {

	public InvalidPayRequestException(final Throwable cause) {
		super(cause.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
