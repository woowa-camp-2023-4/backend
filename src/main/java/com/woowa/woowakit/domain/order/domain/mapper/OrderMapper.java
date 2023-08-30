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
import com.woowa.woowakit.domain.order.exception.ProductNotFoundException;
import com.woowa.woowakit.domain.order.exception.ProductNotOnSaleException;
import com.woowa.woowakit.domain.order.exception.QuantityNotEnoughException;
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

	private OrderItem mapOrderItemFrom(final OrderCreateRequest item, final Product product) {

		validatePurchasable(product, Quantity.from(item.getQuantity()));

		return OrderItem.of(
			item.getProductId(),
			product.getName().getName(),
			Image.from(product.getImageUrl().getPath()),
			product.getPrice().getPrice(),
			Quantity.from(item.getQuantity())
		);
	}

	private Map<Long, Product> findProductsByIds(final List<Long> productIds) {
		Map<Long, Product> productByIds = productRepository.findAllById(productIds)
			.stream()
			.collect(Collectors.toUnmodifiableMap(Product::getId, product -> product));

		validateProductExists(productIds, productByIds);

		return productByIds;
	}

	private void validateProductExists(List<Long> productIds, Map<Long, Product> map) {
		if (map.size() != productIds.size()) {
			throw new ProductNotFoundException();
		}
	}

	private List<Long> collectProductIds(final List<OrderCreateRequest> request) {
		return request.stream()
			.map(OrderCreateRequest::getProductId)
			.collect(Collectors.toUnmodifiableList());
	}

	private void validatePurchasable(final Product product, final Quantity quantity) {
		if (!product.isOnSale()) {
			throw new ProductNotOnSaleException();
		}

		if (!product.isEnoughQuantity(quantity)) {
			throw new QuantityNotEnoughException();
		}
	}
}
