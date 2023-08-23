package com.woowa.woowakit.domain.payment.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final PaymentRepository paymentRepository;
	private final PaymentService paymentService;

	@Transactional
	@EventListener
	public void handle(final OrderCompleteEvent event) {
		payOrder(event);
	}

	private void payOrder(OrderCompleteEvent event) {
		Order order = event.getOrder();
		paymentService.validatePayment(
			event.getPaymentKey(),
			order.getUuid(),
			order.getTotalPrice());
		Payment payment = Payment.of(event.getPaymentKey(), order.getTotalPrice(), order.getUuid(),
			order.getId());

		paymentRepository.save(payment);
	}
}
