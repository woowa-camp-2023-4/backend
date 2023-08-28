package com.woowa.woowakit.domain.payment.domain;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.order.domain.Order;

@Component
public class PaymentMapper {

	public Payment mapFrom(final Order order, final String paymentKey) {
		return Payment.of(
			paymentKey,
			order.getTotalPrice(),
			order.getUuid(),
			order.getId()
		);
	}
}
