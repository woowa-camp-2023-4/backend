package com.woowa.woowakit.domain.payment.domain;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderPayService;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.OrderRollbackService;
import com.woowa.woowakit.global.error.WooWaException;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPayServiceImpl implements OrderPayService {

	private final PaymentClient paymentClient;
	private final PaymentCreateService paymentSaveService;
	private final OrderRepository orderRepository;
	private final OrderRollbackService orderRollbackService;

	@Counted("order.payment.request")
	public void pay(final Long orderId, final String paymentKey) {
		log.info("결제 요청 subscribe event: {}", paymentKey);

		Order order = orderRepository.findById(orderId).orElseThrow();

		paymentClient.validatePayment(paymentKey, order.getUuid(), order.getTotalPrice())
			.publishOn(Schedulers.boundedElastic())
			.doOnSuccess(ignore -> paymentSaveService.save(orderId, paymentKey))
			.doOnError(error -> orderRollbackService.rollback(orderId, error))
			.onErrorMap(IllegalArgumentException.class,
				error -> new WooWaException(error.getMessage(), HttpStatus.BAD_REQUEST))
			.onErrorMap(IllegalStateException.class,
				error -> new WooWaException(error.getMessage()))
			.block();
	}
}
