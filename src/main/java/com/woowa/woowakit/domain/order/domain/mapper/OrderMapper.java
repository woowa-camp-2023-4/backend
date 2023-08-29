package com.woowa.woowakit.domain.order.domain.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.cart.domain.CartItemRepository;
import com.woowa.woowakit.domain.cart.domain.CartItemSpecification;
import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Money;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.exception.CartItemNotFoundException;
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMapper {

	private final ProductRepository productRepository;
	private final CartItemRepository cartItemRepository;

	public Order mapFrom(final Long memberId, final Long productId, final Long quantity) {
		Product product = getProductById(productId);
		OrderItem orderItem = OrderItem.of(
			product.getId(),
			product.getName().getName(),
			Image.from(product.getImageUrl().getPath()),
			product.getPrice().getPrice(),
			Quantity.from(quantity)
		);

		return Order.of(memberId, List.of(orderItem));
	}

	public Order mapFrom(final Long memberId, final List<Long> cartItemIds) {
		List<OrderItem> orderItems = cartItemIds.stream()
			.map(id -> cartItemSpecificationOf(memberId, id))
			.map(this::orderItemOf)
			.collect(Collectors.toUnmodifiableList());

		return Order.of(memberId, orderItems);
	}

	private CartItemSpecification cartItemSpecificationOf(final Long memberId, final Long id) {
		return cartItemRepository.findCartItemByIdAndMemberId(memberId, id)
			.orElseThrow(CartItemNotFoundException::new);
	}

	private OrderItem orderItemOf(final CartItemSpecification cartItem) {
		return OrderItem.of(cartItem.getProductId(),
			cartItem.getProductName(),
			Image.from(cartItem.getProductImage()),
			Money.from(cartItem.getProductPrice()),
			Quantity.from(cartItem.getQuantity()));
	}

	private Product getProductById(final Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(ProductNotFoundException::new);
	}
}
