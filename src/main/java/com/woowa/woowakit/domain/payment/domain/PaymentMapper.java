package com.woowa.woowakit.domain.payment.domain;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

@Component
public class PaymentMapper {

	public Payment mapFrom(final OrderCompleteEvent event) {
		return Payment.of(
			event.getPaymentKey(),
			event.getOrder().getTotalPrice(),
			event.getOrder().getUuid(),
			event.getOrder().getId()
		);
	}
}
