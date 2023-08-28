package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import com.woowa.woowakit.domain.order.domain.service.OrderPayService;
import com.woowa.woowakit.global.error.WooWaException;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentHandler implements OrderPayService {

	private final PaymentClient paymentClient;
	private final PaymentService paymentService;

	@Counted("order.payment.request")
	public void pay(final OrderCompleteEvent event) {
		log.info("결제 요청 subscribe event: {}", event.getPaymentKey());
		payOrder(event);
	}

	private void payOrder(final OrderCompleteEvent event) {
		Order order = event.getOrder();
		paymentClient.validatePayment(event.getPaymentKey(), order.getUuid(), order.getTotalPrice())
			.publishOn(Schedulers.boundedElastic())
			.doOnSuccess(ignore -> paymentService.handlePaySuccess(event))
			.doOnError(error -> paymentService.handlePayError(event, error))
			.onErrorMap(IllegalArgumentException.class,
				error -> new WooWaException(error.getMessage(), HttpStatus.BAD_REQUEST))
			.onErrorMap(IllegalStateException.class,
				error -> new WooWaException(error.getMessage()))
			.block();


	}
}
