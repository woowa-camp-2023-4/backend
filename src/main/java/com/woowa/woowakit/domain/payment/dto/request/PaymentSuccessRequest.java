package com.woowa.woowakit.domain.payment.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PaymentSuccessRequest {

	private Long orderId;
	private String orderToken;
	private Long totalPrice;
	private String paymentKey;

	public static PaymentSuccessRequest of(
		final Long orderId,
		final String orderToken,
		final Long totalPrice,
		final String paymentKey
	) {
		return new PaymentSuccessRequest(orderId, orderToken, totalPrice, paymentKey);
	}
}
