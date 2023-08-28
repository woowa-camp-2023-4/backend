package com.woowa.woowakit.domain.payment.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.OrderRollbackService;

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

	@Counted("order.payment.success")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handlePaySuccess(final Long orderId, final String paymentKey) {
		Order order = findOrderById(orderId);
		order.pay();

		Payment payment = paymentMapper.mapFrom(order, paymentKey);
		paymentRepository.save(payment);

		log.info("결제 완료 subscribe paymentKey: {}", paymentKey);
	}

	@Counted("order.payment.failure")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handlePayError(final Long orderId, final Throwable error) {
		log.error("결제 실패 복구 시작 orderId: {}, message={}", orderId, error.getMessage());
		Order order = findOrderById(orderId);
		order.rollback(orderRollbackService);
	}

	private Order findOrderById(final Long id) {
		return orderRepository.findById(id).orElseThrow();
	}
}
