package com.woowa.woowakit.domain.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.order.domain.mapper.CartItemMapper;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderRollbackService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final CartItemMapper cartItemMapper;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Counted("order.payment.failure")
	public void rollback(final Long orderId, final Throwable error) {
		log.error("결제 실패 복구 시작 orderId: {}, message={}", orderId, error.getMessage());

		Order order = findOrderById(orderId);
		order.cancel();
		rollbackCartItems(order);
		rollbackProducts(order);
	}

	private void rollbackProducts(final Order order) {
		List<Product> products = productRepository.findByIdsWithPessimistic(order.collectProductIds());
		rollbackProducts(order, products);
	}

	void rollbackProducts(final Order order, final List<Product> products) {
		QuantityOfProducts quantityData = QuantityOfProducts.from(order);

		for (Product product : products) {
			product.addQuantity(quantityData.getFrom(product));
		}
	}

	private void rollbackCartItems(final Order order) {
		List<CartItem> cartItems = cartItemMapper.mapAllFrom(order);
		cartItemRepository.saveAll(cartItems);
	}

	private Order findOrderById(final Long id) {
		return orderRepository.findById(id).orElseThrow();
	}
}
