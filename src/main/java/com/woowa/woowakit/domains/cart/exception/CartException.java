package com.woowa.woowakit.domains.cart.exception;

import org.springframework.http.HttpStatus;

import com.woowa.woowakit.global.error.WooWaException;

public class CartException extends WooWaException {

	public CartException(final HttpStatus httpStatus) {
		super(httpStatus);
	}

	public CartException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CartException(final String message, final Throwable cause, final HttpStatus httpStatus) {
		super(message, cause, httpStatus);
	}

	public CartException(final String message) {
		super(message);
	}

	public CartException(final String message, final HttpStatus httpStatus) {
		super(message, httpStatus);
	}
}
