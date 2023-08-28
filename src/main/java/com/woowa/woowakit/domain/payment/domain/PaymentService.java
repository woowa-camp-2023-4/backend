package com.woowa.woowakit.domain.payment.domain;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.OrderRollbackService;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final PaymentMapper paymentMapper;
	private final OrderRepository orderRepository;
	private final OrderRollbackService orderRollbackService;

	@Counted("order.payment.success")
	@Transactional
	public void handlePaySuccess(final OrderCompleteEvent event) {
		Order order = findOrderById(event.getOrder().getId());
		Payment payment = paymentMapper.mapFrom(event);

		order.pay();
		paymentRepository.save(payment);
		log.info("결제 완료 subscribe paymentKey: {}", event.getPaymentKey());
	}

	@Transactional
	@Counted("order.payment.failure")
	public void handlePayError(final OrderCompleteEvent event, final Throwable error) {
		log.error("결제 실패 복구 시작 paymentKey: {}, message={}", event.getPaymentKey(),
			error.getMessage());
		Order order = findOrderById(event.getOrder().getId());
		order.rollback(orderRollbackService);
	}

	private Order findOrderById(final Long id) {
		return orderRepository.findById(id).orElseThrow();
	}
}
