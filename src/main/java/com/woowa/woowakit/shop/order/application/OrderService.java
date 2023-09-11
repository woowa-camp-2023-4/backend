package com.woowa.woowakit.shop.order.application;

import java.util.List;

import com.woowa.woowakit.shop.order.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.shop.auth.domain.AuthPrincipal;
import com.woowa.woowakit.shop.order.domain.Order;
import com.woowa.woowakit.shop.order.domain.OrderPayService;
import com.woowa.woowakit.shop.order.domain.OrderPlaceService;
import com.woowa.woowakit.shop.order.domain.OrderRepository;
import com.woowa.woowakit.shop.order.domain.mapper.OrderMapper;
import com.woowa.woowakit.shop.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.shop.order.dto.request.OrderPayRequest;
import com.woowa.woowakit.shop.order.dto.request.OrderSearchRequest;
import com.woowa.woowakit.shop.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.shop.order.dto.response.OrderResponse;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final OrderPayService orderPayService;
	private final OrderPlaceService orderPlaceService;

	@Transactional(readOnly = true)
	public OrderDetailResponse findOrderByOrderIdAndMemberId(
		final AuthPrincipal authPrincipal,
		final Long orderId
	) {
		log.info("주문 조회 memberId: {} orderId: {}", authPrincipal.getId(), orderId);
		Order order = getOrderByOrderIdAndMemberId(authPrincipal, orderId);
		return OrderDetailResponse.from(order);
	}

	private Order getOrderByOrderIdAndMemberId(
		final AuthPrincipal authPrincipal,
		final Long orderId
	) {
		return orderRepository.findOrderById(orderId, authPrincipal.getId())
			.orElseThrow(OrderNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public List<OrderDetailResponse> findAllOrderByMemberId(
		final AuthPrincipal authPrincipal,
		final OrderSearchRequest request
	) {
		log.info("주문 목록 조회 memberId: {}", authPrincipal.getId());
		List<Order> orders = orderRepository.findOrdersByMemberId(
			authPrincipal.getId(), request.getLastOrderId(), request.getPageSize());
		return OrderDetailResponse.listOf(orders);
	}

	@Transactional
	@Counted("order.preOrder")
	public OrderResponse create(final AuthPrincipal authPrincipal, final List<OrderCreateRequest> request) {
		log.info("주문 생성 memberId: {}", authPrincipal.getId());
		Order order = orderMapper.mapFrom(authPrincipal.getId(), request);

		orderRepository.save(order);
		return OrderResponse.from(order);
	}

	public void pay(final AuthPrincipal authPrincipal, final Long orderId, final OrderPayRequest request) {
		orderPlaceService.place(authPrincipal, orderId);
		orderPayService.pay(orderId, request.getPaymentKey());
	}
}
