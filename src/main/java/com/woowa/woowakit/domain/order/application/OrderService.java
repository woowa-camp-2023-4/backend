package com.woowa.woowakit.domain.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.auth.domain.AuthPrincipal;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderMapper;
import com.woowa.woowakit.domain.order.domain.OrderRepository;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.request.PreOrderCreateRequest;
import com.woowa.woowakit.domain.order.dto.response.OrderDetailResponse;
import com.woowa.woowakit.domain.order.dto.response.PreOrderResponse;
import com.woowa.woowakit.domain.order.exception.OrderNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public OrderDetailResponse findOrderByOrderIdAndMemberId(final AuthPrincipal authPrincipal, final Long orderId) {
        Order order = getOrderByOrderIdAndMemberId(authPrincipal, orderId);
        return OrderDetailResponse.from(order);
    }

    private Order getOrderByOrderIdAndMemberId(final AuthPrincipal authPrincipal, final Long orderId) {
        return orderRepository.findOrderById(orderId, authPrincipal.getId())
            .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> findAllOrderByMemberId(final AuthPrincipal authPrincipal) {
        List<Order> orders = orderRepository.findAllByMemberId(authPrincipal.getId());
        return OrderDetailResponse.listOf(orders);
    }

	@Transactional
	public PreOrderResponse preOrder(final AuthPrincipal authPrincipal, final PreOrderCreateRequest request) {
		Order order = orderMapper.mapFrom(authPrincipal.getId(), request.getProductId(), request.getQuantity());
		return PreOrderResponse.from(orderRepository.save(order));
	}

	@Transactional
	public Long order(final AuthPrincipal authPrincipal, final OrderCreateRequest request) {
		Order order = getOrderById(authPrincipal.getId(), request.getOrderId());
		order.order(request.getPaymentKey());
		return orderRepository.save(order).getId();
	}

	private Order getOrderById(final Long memberId, final Long orderId) {
		return orderRepository.findByIdAndMemberId(orderId, memberId)
			.orElseThrow(OrderNotFoundException::new);
	}
}
