package com.woowa.woowakit.infra.payment.toss.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TossPaymentRequest {
	private String paymentKey;
	private String orderId;
	private long amount;

	public static TossPaymentRequest of(final String paymentKey, final String orderId, final long amount) {
		return new TossPaymentRequest(paymentKey, orderId, amount);
	}
}
