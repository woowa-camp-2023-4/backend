package com.woowa.woowakit.domain.order.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderMapper;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	@Transactional
	public PreOrderResponse preOrder(final Long memberId, final PreOrderCreateRequest request) {
		Order order = orderMapper.mapFrom(memberId, request.getProductId(), request.getQuantity());
		return PreOrderResponse.from(orderRepository.save(order));
	}

	@Transactional
	public Long order(final Long memberId, final OrderCreateRequest request) {
		Order order = getOrderById(memberId, request.getOrderId());
		order.order(request.getPaymentKey());
		return orderRepository.save(order).getId();
	}

	private Order getOrderById(final Long memberId, final Long orderId) {
		return orderRepository.findByIdAndMemberId(orderId, memberId)
			.orElseThrow(OrderNotFoundException::new);
	}
}
