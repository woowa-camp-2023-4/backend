package com.woowa.woowakit.domain.payment.domain;

public class PaymentSuccessEvent {
	private final Payment payment;

	public PaymentSuccessEvent(final Payment payment) {
		this.payment = payment;
	}
}
