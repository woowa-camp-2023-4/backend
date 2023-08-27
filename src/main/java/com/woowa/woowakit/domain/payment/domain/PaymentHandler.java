package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final PaySaveService paySaveService;
	private final PaymentService paymentService;

	@EventListener
	@Counted("order.payment.request")
	public void handle(final OrderCompleteEvent event) {
		log.info("결제 요청 subscribe event: {}", event.getPaymentKey());
		payOrder(event);
	}

	private void payOrder(final OrderCompleteEvent event) {
		Order order = event.getOrder();
		Mono<Void> payMono = paymentService.validatePayment(
			event.getPaymentKey(),
			order.getUuid(),
			order.getTotalPrice());
		paySaveService.save(event, payMono);
	}
}
