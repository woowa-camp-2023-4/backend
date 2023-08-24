package com.woowa.woowakit.domain.order.domain.handler;

import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.domain.event.OrderCompleteEvent;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
			log.info("상품 {}의 수량을 {}만큼 감소시킵니다. 현재 상품 수량: {}", product.getId(),
				quantityOfProducts.get(product.getId()), product.getQuantity().getValue());

			product.subtractQuantity(quantityOfProducts.get(product.getId()));

			log.info("상품 {}의 수량이 {}로 변경되었습니다.", product.getId(), product.getQuantity().getValue());
		}
	}

	private Map<Long, Quantity> initializeQuantityOfProducts(final List<OrderItem> orderItems) {
		return orderItems.stream()
			.collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));
	}
}
