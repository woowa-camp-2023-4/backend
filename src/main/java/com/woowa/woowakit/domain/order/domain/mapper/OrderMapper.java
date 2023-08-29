package com.woowa.woowakit.domain.order.domain.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.woowa.woowakit.domain.model.Image;
import com.woowa.woowakit.domain.model.Quantity;
import com.woowa.woowakit.domain.order.domain.Order;
import com.woowa.woowakit.domain.order.domain.OrderItem;
import com.woowa.woowakit.domain.order.dto.request.OrderCreateRequest;
import com.woowa.woowakit.domain.product.domain.product.Product;
import com.woowa.woowakit.domain.product.domain.product.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMapper {

	private final ProductRepository productRepository;

	public Order mapFrom(final Long memberId, final List<OrderCreateRequest> request) {
		List<Long> productIds = collectProductIds(request);
		Map<Long, Product> products = findProductsByIds(productIds);

		return Order.of(memberId, request.stream()
			.map(item -> mapOrderItemFrom(item, products.get(item.getProductId())))
			.collect(Collectors.toList()));
	}

	private Map<Long, Product> findProductsByIds(List<Long> productIds) {
		return productRepository.findAllById(productIds)
			.stream()
			.collect(Collectors.toUnmodifiableMap(Product::getId, product -> product));
	}

	private List<Long> collectProductIds(List<OrderCreateRequest> request) {
		return request.stream()
			.map(OrderCreateRequest::getProductId)
			.collect(Collectors.toUnmodifiableList());
	}

	private OrderItem mapOrderItemFrom(final OrderCreateRequest item, final Product product) {
		return OrderItem.of(
			item.getProductId(),
			product.getName().getName(),
			Image.from(product.getImageUrl().getPath()),
			product.getPrice().getPrice(),
			Quantity.from(item.getQuantity())
		);
	}
}
