package com.woowa.woowakit.domain.order.domain.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;

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
