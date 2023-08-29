package com.woowa.woowakit.domain.order.domain;

import com.woowa.woowakit.domain.order.exception.InvalidPayRequestException;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;
import com.woowa.woowakit.domain.order.exception.PayFailedException;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPayService {

	private final PaymentClient paymentClient;
	private final OrderRepository orderRepository;
	private final PayResultHandler payResultHandler;
	private final Scheduler boundedElasticScheduler = Schedulers.newBoundedElastic(100,
		Integer.MAX_VALUE, "order.pay");

	@Counted("order.payment.request")
	public void pay(final Long orderId, final String paymentKey) {
		log.info("결제 요청 subscribe event: {}", paymentKey);

		Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

		paymentClient.validatePayment(paymentKey, order.getUuid(), order.getTotalPrice())
			.publishOn(boundedElasticScheduler)
			.doOnSuccess(ignore -> payResultHandler.save(orderId, paymentKey))
			.doOnError(error -> payResultHandler.rollback(orderId, error))
			.onErrorMap(IllegalArgumentException.class, InvalidPayRequestException::new)
			.onErrorMap(IllegalStateException.class, PayFailedException::new)
			.subscribe();
	}
}
