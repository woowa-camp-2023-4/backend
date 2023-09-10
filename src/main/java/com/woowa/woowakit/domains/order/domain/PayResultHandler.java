package com.woowa.woowakit.domains.order.domain;

import java.util.List;

import com.woowa.woowakit.domains.cart.domain.CartItem;
import com.woowa.woowakit.domains.cart.domain.CartItemRepository;
import com.woowa.woowakit.domains.order.domain.mapper.CartItemMapper;
import com.woowa.woowakit.domains.order.exception.OrderNotFoundException;
import com.woowa.woowakit.domains.product.domain.product.Product;
import com.woowa.woowakit.domains.product.domain.product.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayResultHandler {

	private final CartItemRepository cartItemRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final PaymentSaveService paymentSaveService;
	private final CartItemMapper cartItemMapper;

	@Transactional
	@Counted("order.payment.success")
	public void save(final Long orderId, final String paymentKey) {
		log.info("결제 성공 orderId: {}, paymentKey={}", orderId, paymentKey);
		Order order = findOrderById(orderId);

		order.pay();
		paymentSaveService.save(orderId, order.getTotalPrice(), paymentKey);
	}

	@Transactional
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
		return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
	}
}
