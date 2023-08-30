package com.woowa.woowakit.domain.order.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.woowa.woowakit.domain.order.domain.Order;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderResponse {

	private Long id;
	private List<OrderItemResponse> orderItems;
	private String uuid;

	public static OrderResponse of(final Long id, final List<OrderItemResponse> orderItems,
		final String uuid) {
		return new OrderResponse(id, orderItems, uuid);
	}

	public static OrderResponse from(final Order order) {
		List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
			.map(OrderItemResponse::from)
			.collect(Collectors.toUnmodifiableList());

		return OrderResponse.of(
			order.getId(),
			orderItemResponses,
			order.getUuid()
		);
	}
}
