package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final PaymentRepository paymentRepository;
	private final PaymentService paymentService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
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

		paymentRepository.save(payment);
	}
}
