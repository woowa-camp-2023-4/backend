package com.woowa.woowakit.domains.product.exception;

import org.springframework.http.HttpStatus;

import com.woowa.woowakit.global.error.WooWaException;

public class ProductException extends WooWaException {
	public ProductException(HttpStatus httpStatus) {
		super(httpStatus);
	}

	public ProductException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProductException(String message, Throwable cause, HttpStatus httpStatus) {
		super(message, cause, httpStatus);
	}

	public ProductException(String message) {
		super(message);
	}

	public ProductException(String message, HttpStatus httpStatus) {
		super(message, httpStatus);
	}
}
