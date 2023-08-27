package com.woowa.woowakit.domain.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowa.woowakit.domain.cart.domain.CartItem;
import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderRollbackService {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final CartItemMapper cartItemMapper;

	@Transactional
	public void rollback(final Order order) {
		rollbackCartItems(order);
		rollbackProducts(order);
	}

	private void rollbackProducts(final Order order) {
		List<Product> products = productRepository.findByIdsWithPessimistic(order.collectProductIds());
		rollbackProducts(order, products);
	}

	void rollbackProducts(final Order order, final List<Product> products) {
		ProductIdToQuantity quantityData = ProductIdToQuantity.from(order);

		for (Product product : products) {
			product.addQuantity(quantityData.getFrom(product));
		}
	}

	private void rollbackCartItems(final Order order) {
		List<CartItem> cartItems = cartItemMapper.mapAllFrom(order);
		cartItemRepository.saveAll(cartItems);
	}
}
