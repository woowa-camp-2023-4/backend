package com.woowa.woowakit.domain.order.domain.service;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import com.woowa.woowakit.domain.order.domain.handler.CartItemDeletionEventHandler;
import com.woowa.woowakit.domain.order.domain.handler.ProductQuantityEventHandler;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderOrderService {

	private final OrderRepository orderRepository;
	private final CartItemDeletionEventHandler cartItemDeletionEventHandler;
	private final ProductQuantityEventHandler productQuantityEventHandler;

	@Transactional
	@Counted("order.order")
	public OrderCompleteEvent order(final AuthPrincipal authPrincipal,
		final OrderCreateRequest request) {
		log.info("주문 생성 memberId: {} orderId: {} paymentKey: {}", authPrincipal.getId(),
			request.getOrderId(), request.getPaymentKey());
		Order order = getOrderById(authPrincipal.getId(), request.getOrderId());
		order.order();
		OrderCompleteEvent orderCompleteEvent = new OrderCompleteEvent(order,
			request.getPaymentKey());

		cartItemDeletionEventHandler.handle(orderCompleteEvent);
		productQuantityEventHandler.handle(orderCompleteEvent);

		return orderCompleteEvent;
	}

	private Order getOrderById(final Long memberId, final Long orderId) {
		return orderRepository.findByIdAndMemberId(orderId, memberId)
			.orElseThrow(OrderNotFoundException::new);
	}

}
