package com.woowa.woowakit.shop.order.dto.response;

import com.woowa.woowakit.shop.order.domain.OrderItem;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderItemResponse {

	private Long id;
	private Long productId;
	private String name;
	private String image;
	private Long price;
	private Long quantity;

	public static OrderItemResponse of(
		final Long id,
		final Long productId,
		final String name,
		final String image,
		final Long price,
		final Long quantity
	) {
		return new OrderItemResponse(id, productId, name, image, price, quantity);
	}

	public static OrderItemResponse from(final OrderItem orderItem) {
		return OrderItemResponse.of(
			orderItem.getId(),
			orderItem.getProductId(),
			orderItem.getName(),
			orderItem.getImage().getValue(),
			orderItem.getPrice().getValue(),
			orderItem.getQuantity().getValue()
		);
	}
}
