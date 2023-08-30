package com.woowa.woowakit.global.error;

import org.springframework.http.HttpStatus;

public class UnAuthorizationException extends WooWaException {

	public UnAuthorizationException() {
		super("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
	}
}
