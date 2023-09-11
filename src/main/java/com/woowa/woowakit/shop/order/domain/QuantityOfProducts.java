package com.woowa.woowakit.shop.order.domain;

import java.util.Map;
import java.util.stream.Collectors;

import com.woowa.woowakit.shop.model.Quantity;
import com.woowa.woowakit.shop.product.domain.product.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuantityOfProducts {

	private final Map<Long, Quantity> data;

	public static QuantityOfProducts from(final Order order) {
		return new QuantityOfProducts(order.getOrderItems()
			.stream()
			.collect(Collectors.toUnmodifiableMap(OrderItem::getProductId, OrderItem::getQuantity)));
	}

	public Quantity getFrom(final Product product) {
		return data.get(product.getId());
	}
}
