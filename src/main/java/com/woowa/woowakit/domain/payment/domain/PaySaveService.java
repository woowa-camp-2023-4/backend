package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaySaveService {

	private final PaymentRepository paymentRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Async
	@Counted("order.payment.async")
	public void save(final OrderCompleteEvent event, final Mono<Void> pay) {
		pay.block();
		Payment payment = mapToPayment(event);
		paymentRepository.save(payment);
		log.info("결제 완료 subscribe paymentKey: {}", event.getPaymentKey());
	}

	private Payment mapToPayment(OrderCompleteEvent event) {
		return Payment.of(
			event.getPaymentKey(),
			event.getOrder().getTotalPrice(),
			event.getOrder().getUuid(),
			event.getOrder().getId()
		);
	}
}
