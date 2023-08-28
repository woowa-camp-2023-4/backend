package com.woowa.woowakit.domain.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderPayService;
import com.woowa.woowakit.domain.order.domain.OrderPlaceService;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.domain.mapper.OrderMapper;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateCartItemRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;

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
	public List<OrderDetailResponse> findAllOrderByMemberId(final AuthPrincipal authPrincipal) {
		log.info("주문 목록 조회 memberId: {}", authPrincipal.getId());
		List<Order> orders = orderRepository.findAllByMemberId(authPrincipal.getId());
		return OrderDetailResponse.listOf(orders);
	}

	@Transactional
	@Counted("order.preOrder")
	public PreOrderResponse preOrder(final AuthPrincipal authPrincipal,
		final PreOrderCreateRequest request) {
		log.info("가주문 생성 memberId: {} productId: {} quantity: {}", authPrincipal.getId(),
			request.getProductId(), request.getQuantity());
		Order order = orderMapper.mapFrom(authPrincipal.getId(), request.getProductId(),
			request.getQuantity());
		return PreOrderResponse.from(orderRepository.save(order));
	}

	@Transactional
	public PreOrderResponse preOrderCartItems(
		final AuthPrincipal authPrincipal,
		final List<PreOrderCreateCartItemRequest> requests
	) {
		log.info("장바구니로 가주문 생성 memberId: {}", authPrincipal.getId());
		List<Long> cartItemIds = PreOrderCreateCartItemRequest.toCartItemIds(requests);
		Order order = orderMapper.mapFrom(authPrincipal.getId(), cartItemIds);
		return PreOrderResponse.from(orderRepository.save(order));
	}

	public Long order(final AuthPrincipal authPrincipal, final OrderCreateRequest request) {
		orderPlaceService.order(authPrincipal, request.getOrderId());
		orderPayService.pay(request.getOrderId(), request.getPaymentKey());
		return request.getOrderId();
	}
}
