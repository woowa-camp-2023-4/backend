package com.woowa.woowakit.domain.product.domain.product;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.exception.ProductNotExistException;
import com.woowa.woowakit.domain.order.domain.OrderCompleteEvent;
import com.woowa.woowakit.domain.order.domain.OrderItem;

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
		for (OrderItem orderItem : orderItems) {
			Product product = getProductWithPessimistic(orderItem);
			product.subtractQuantity(orderItem.getQuantity());
		}
	}

	private Product getProductWithPessimistic(final OrderItem orderItem) {
		return productRepository.findByIdWithPessimistic(orderItem.getProductId())
			.orElseThrow(ProductNotExistException::new);
	}
}
