package com.woowa.woowakit.domain.order.domain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

	private final ProductRepository productRepository;

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

	private Product getProductById(final Long productId) {
		return productRepository.findById(productId)
			.orElseThrow(ProductNotFoundException::new);
	}
}
