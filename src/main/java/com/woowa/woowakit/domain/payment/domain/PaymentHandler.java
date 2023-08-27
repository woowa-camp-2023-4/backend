package com.woowa.woowakit.domain.payment.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler {

	private final PaymentClient paymentClient;
	private final PaymentService paymentService;

	@EventListener
	@Counted("order.payment.request")
	public void handle(final OrderCompleteEvent event) {
		log.info("결제 요청 subscribe event: {}", event.getPaymentKey());
		payOrder(event);
	}

	private void payOrder(final OrderCompleteEvent event) {
		Order order = event.getOrder();
		paymentClient.validatePayment(event.getPaymentKey(), order.getUuid(), order.getTotalPrice())
			.publishOn(Schedulers.boundedElastic())
			.doOnSuccess(ignore -> paymentService.handlePaySuccess(event))
			.doOnError(error -> paymentService.handlePayError(event, error))
			.subscribe();
	}
}
