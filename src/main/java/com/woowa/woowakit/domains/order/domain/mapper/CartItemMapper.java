package com.woowa.woowakit.domains.order.domain.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.woowa.woowakit.domains.order.domain.Order;
import com.woowa.woowakit.domains.order.domain.OrderItem;
import org.springframework.stereotype.Component;

import com.woowa.woowakit.domains.cart.domain.CartItem;

@Component
public class CartItemMapper {

	public List<CartItem> mapAllFrom(final Order order) {
		return order.getOrderItems()
			.stream()
			.map(orderItem -> mapFrom(orderItem, order.getMemberId()))
			.collect(Collectors.toList());
	}

	private CartItem mapFrom(final OrderItem orderItem, final Long memberId) {
		return CartItem.of(memberId, orderItem.getProductId(), orderItem.getQuantity().getValue());
	}
}
