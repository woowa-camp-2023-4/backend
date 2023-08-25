package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final PaySaveService paySaveService;
	private final PaymentService paymentService;

	@TransactionalEventListener
	public void handle(final OrderCompleteEvent event) {
		log.info("결제 요청 subscribe event: {}", event.getPaymentKey());
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

		paySaveService.save(payment);
		log.info("결제 완료 subscribe event: {}");
	}
}
