package com.woowa.woowakit.domain.order.application;

public interface PaymentService {
	void validatePayment(String paymentKey, String orderToken, long totalPrice);
}
