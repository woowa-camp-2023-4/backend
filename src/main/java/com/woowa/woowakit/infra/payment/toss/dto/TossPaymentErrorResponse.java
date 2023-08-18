package com.woowa.woowakit.infra.payment.toss.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TossPaymentErrorResponse {

	private String code;
	private String message;

	public static TossPaymentErrorResponse of(final String code, final String message) {
		return new TossPaymentErrorResponse(code, message);
	}
}
