package com.woowa.woowakit.domain.order.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class OrderCompleteEvent {

	private final Order order;
	private final String paymentKey;

	public OrderCompleteEvent(final Order order, final String paymentKey) {
		this.order = order;
		this.paymentKey = paymentKey;
	}

	public List<OrderItem> getOrderItem() {
		return order.getOrderItems();
	}

	public Order getOrder() {
		return order;
	}
}
