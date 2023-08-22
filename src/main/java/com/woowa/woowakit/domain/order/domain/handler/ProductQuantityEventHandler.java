package com.woowa.woowakit.domain.order.domain.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductQuantityEventHandler {

	private final ProductRepository productRepository;

	@Order(0)
	@Transactional
	@EventListener
	public void handle(final OrderCompleteEvent event) {
		subtractProductQuantity(event);
	}

	private void subtractProductQuantity(final OrderCompleteEvent event) {
		List<OrderItem> orderItems = event.getOrderItem();
		Map<Long, Quantity> quantityOfProducts = initializeQuantityOfProducts(orderItems);
		List<Long> productIds = orderItems.stream()
			.map(OrderItem::getProductId)
			.collect(Collectors.toList());

		List<Product> products = productRepository.findByIdsWithPessimistic(productIds);
		for (Product product : products) {
			product.subtractQuantity(quantityOfProducts.get(product.getId()));
		}
	}

	private Map<Long, Quantity> initializeQuantityOfProducts(final List<OrderItem> orderItems) {
		return orderItems.stream()
			.collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));
	}
}
