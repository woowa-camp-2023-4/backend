package com.woowa.woowakit.domain.payment.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.OrderRollbackService;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;
	private final OrderRepository orderRepository;
	private final OrderRollbackService orderRollbackService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Counted("order.payment.async")
	public void handlePaySuccess(final OrderCompleteEvent event) {
		Payment payment = paymentMapper.mapFrom(event);
		paymentRepository.save(payment);
		log.info("결제 완료 subscribe paymentKey: {}", event.getPaymentKey());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handlePayError(final OrderCompleteEvent event, final Throwable error) {
		log.error("결제 실패 복구 시작 paymentKey: {}, message={}", event.getPaymentKey(), error.getMessage());
		Order order = orderRepository.findById(event.getOrder().getId()).orElseThrow();
		order.rollback(orderRollbackService);
	}
}
