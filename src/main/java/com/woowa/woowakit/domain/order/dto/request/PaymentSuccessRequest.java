package com.woowa.woowakit.domain.order.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PaymentSuccessRequest {

	private String orderToken;
	private String paymentKey;

	public static PaymentSuccessRequest of(
		final String orderToken,
		final String paymentKey
	) {
		return new PaymentSuccessRequest(orderToken, paymentKey);
	}
}
