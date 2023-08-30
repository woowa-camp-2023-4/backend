package com.woowa.woowakit.domain.order.exception;

import org.springframework.http.HttpStatus;

public class PayFailedException extends OrderException {

	public PayFailedException(final Throwable cause) {
		super(cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
